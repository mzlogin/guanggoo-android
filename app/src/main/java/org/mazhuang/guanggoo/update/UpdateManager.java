package org.mazhuang.guanggoo.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.data.entity.UpdateInfo;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;
import org.mazhuang.guanggoo.util.VersionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 应用自升级：每天首次启动静默检测，检测到新版本后提示并自动下载安装。
 *
 * @author mazhuang
 */
public class UpdateManager {

    private static final String KEY_LAST_CHECK_DATE = "key_update_last_check_date";
    private static final String KEY_DOWNLOAD_ID = "key_update_download_id";
    private static final String KEY_DOWNLOAD_PATH = "key_update_download_path";
    private static final String KEY_DOWNLOAD_MD5 = "key_update_download_md5";
    private static final String KEY_PENDING_INSTALL_PATH = "key_update_pending_install_path";
    private static final String KEY_FORCE_UPDATE_PENDING = "key_update_force_pending";

    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    private static final String UPDATE_DIR_NAME = "guanggoo-update";
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 15000;

    private static final UpdateManager INSTANCE = new UpdateManager();

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Gson mGson = new Gson();

    private WeakReference<Activity> mActivityRef;
    private BroadcastReceiver mDownloadReceiver;
    private String mPendingInstallPath;
    private UpdateInfo mLastUpdateInfo;
    private boolean mDialogShowing;
    private boolean mChecking;

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        return INSTANCE;
    }

    /**
     * 每天第一次进入主界面时静默检测一次。
     */
    public void checkDaily(Activity activity) {
        mActivityRef = new WeakReference<>(activity);

        Context appContext = activity.getApplicationContext();
        String today = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        // 强制更新未完成时，每次启动都重新检测
        boolean forceUpdatePending =
                PrefsUtil.getBoolean(appContext, KEY_FORCE_UPDATE_PENDING, false);
        if (!forceUpdatePending
                && today.equals(PrefsUtil.getString(appContext, KEY_LAST_CHECK_DATE, ""))) {
            return;
        }
        PrefsUtil.putString(appContext, KEY_LAST_CHECK_DATE, today);
        checkForUpdate(appContext, false);
    }

    /**
     * 用户主动检查更新，会给出检测结果提示。
     */
    public void checkManually(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
        checkForUpdate(activity.getApplicationContext(), true);
    }

    /**
     * 界面回到前台时恢复未完成的安装流程。
     */
    public void onActivityResumed(Activity activity) {
        mActivityRef = new WeakReference<>(activity);

        if (mPendingInstallPath != null) {
            // 从“安装未知应用”设置页返回，权限已授予则继续安装
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                    || activity.getPackageManager().canRequestPackageInstalls()) {
                installApk(activity, new File(mPendingInstallPath));
            }
            return;
        }

        // 进程被系统回收后重新进入，恢复对之前未完成下载的监听
        Context appContext = activity.getApplicationContext();
        long downloadId = PrefsUtil.getLong(appContext, KEY_DOWNLOAD_ID, -1);
        if (downloadId != -1 && mDownloadReceiver == null) {
            registerDownloadReceiver(appContext);
            handleDownloadComplete(appContext, downloadId);
            return;
        }

        // 强制更新未完成时，回到前台重新提示
        maybeRePromptForcedUpdate(activity);
    }

    private void checkForUpdate(final Context appContext, final boolean manual) {
        if (mChecking) {
            return;
        }
        mChecking = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateInfo info = null;
                try {
                    info = requestUpdateInfo(appContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final UpdateInfo updateInfo = info;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mChecking = false;

                        if (updateInfo == null) {
                            if (manual) {
                                toast(appContext, R.string.update_check_failed);
                            }
                            return;
                        }

                        if (!isNewerVersion(appContext, updateInfo)) {
                            PrefsUtil.putBoolean(appContext, KEY_FORCE_UPDATE_PENDING, false);
                            mLastUpdateInfo = null;
                            if (manual) {
                                toast(appContext, R.string.update_is_latest);
                            }
                            return;
                        }

                        PrefsUtil.putBoolean(appContext, KEY_FORCE_UPDATE_PENDING,
                                updateInfo.forceUpdate);
                        mLastUpdateInfo = updateInfo;

                        Activity activity = getActivity();
                        if (activity != null) {
                            showUpdateDialog(activity, updateInfo);
                        }
                    }
                });
            }
        }, "update-check").start();
    }

    private UpdateInfo requestUpdateInfo(Context context) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(ConstantUtil.UPDATE_CHECK_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent",
                    "guanggoo-android/" + VersionUtil.getVersionCode(context));

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } finally {
                reader.close();
            }

            UpdateInfo info = mGson.fromJson(builder.toString(), UpdateInfo.class);
            if (info == null || info.versionCode <= 0 || TextUtils.isEmpty(info.downloadUrl)) {
                return null;
            }

            String scheme = Uri.parse(info.downloadUrl).getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                return null;
            }
            return info;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean isNewerVersion(Context context, UpdateInfo info) {
        return info.versionCode > VersionUtil.getVersionCode(context);
    }

    private void showUpdateDialog(final Activity activity, final UpdateInfo info) {
        if (mDialogShowing) {
            return;
        }

        String version = TextUtils.isEmpty(info.versionName)
                ? String.valueOf(info.versionCode) : info.versionName;
        String message = TextUtils.isEmpty(info.changeLog)
                ? activity.getString(R.string.update_found_message) : info.changeLog;
        if (info.forceUpdate) {
            message = message + "\n\n" + activity.getString(R.string.update_force_tip);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.update_found_title, version))
                .setMessage(message)
                .setPositiveButton(R.string.update_now,
                        (dialog, which) -> downloadApk(activity, info));
        if (info.forceUpdate) {
            builder.setCancelable(false);
        } else {
            builder.setNegativeButton(R.string.update_later, null);
        }

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(d -> mDialogShowing = false);
        mDialogShowing = true;
        dialog.show();
    }

    /**
     * 强制更新未完成时，每次回到前台重新提示。
     */
    private void maybeRePromptForcedUpdate(Activity activity) {
        Context appContext = activity.getApplicationContext();
        if (!PrefsUtil.getBoolean(appContext, KEY_FORCE_UPDATE_PENDING, false)) {
            return;
        }
        if (mDialogShowing || mPendingInstallPath != null) {
            return;
        }

        long downloadId = PrefsUtil.getLong(appContext, KEY_DOWNLOAD_ID, -1);
        if (downloadId != -1) {
            int status = queryDownloadStatus(appContext, downloadId);
            if (status == DownloadManager.STATUS_PENDING
                    || status == DownloadManager.STATUS_RUNNING
                    || status == DownloadManager.STATUS_PAUSED
                    || status == DownloadManager.STATUS_SUCCESSFUL) {
                return;
            }
        }

        if (mLastUpdateInfo != null && isNewerVersion(appContext, mLastUpdateInfo)) {
            showUpdateDialog(activity, mLastUpdateInfo);
        } else {
            checkForUpdate(appContext, false);
        }
    }

    private void downloadApk(Activity activity, UpdateInfo info) {
        Context appContext = activity.getApplicationContext();

        // 已有下载任务在进行中时不重复下载
        long existingId = PrefsUtil.getLong(appContext, KEY_DOWNLOAD_ID, -1);
        if (existingId != -1) {
            int status = queryDownloadStatus(appContext, existingId);
            if (status == DownloadManager.STATUS_PENDING
                    || status == DownloadManager.STATUS_RUNNING
                    || status == DownloadManager.STATUS_PAUSED) {
                registerDownloadReceiver(appContext);
                toast(appContext, R.string.update_downloading);
                return;
            }
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                registerDownloadReceiver(appContext);
                handleDownloadComplete(appContext, existingId);
                return;
            }
            clearDownloadState(appContext);
        }

        File dir = getUpdateDir(appContext);
        if (dir == null) {
            onDownloadFailed(appContext);
            return;
        }
        cleanOldApks(dir);

        String fileName = "guanggoo-" + info.versionCode + ".apk";
        File apkFile = new File(dir, fileName);
        if (apkFile.exists() && !apkFile.delete()) {
            onDownloadFailed(appContext);
            return;
        }

        String version = TextUtils.isEmpty(info.versionName)
                ? String.valueOf(info.versionCode) : info.versionName;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.downloadUrl));
        request.setTitle(appContext.getString(R.string.app_name));
        request.setDescription(appContext.getString(R.string.update_download_description, version));
        request.setMimeType(APK_MIME_TYPE);
        request.setAllowedOverRoaming(false);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(appContext, Environment.DIRECTORY_DOWNLOADS,
                UPDATE_DIR_NAME + "/" + fileName);

        DownloadManager downloadManager =
                (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            onDownloadFailed(appContext);
            return;
        }

        try {
            registerDownloadReceiver(appContext);

            long downloadId = downloadManager.enqueue(request);
            PrefsUtil.putLong(appContext, KEY_DOWNLOAD_ID, downloadId);
            PrefsUtil.putString(appContext, KEY_DOWNLOAD_PATH, apkFile.getAbsolutePath());
            PrefsUtil.putString(appContext, KEY_DOWNLOAD_MD5,
                    info.md5 == null ? "" : info.md5);

            toast(appContext, R.string.update_downloading);
        } catch (Exception e) {
            e.printStackTrace();
            onDownloadFailed(appContext);
        }
    }

    private void registerDownloadReceiver(Context appContext) {
        if (mDownloadReceiver != null) {
            return;
        }

        mDownloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId != -1) {
                    handleDownloadComplete(context.getApplicationContext(), downloadId);
                }
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                appContext.registerReceiver(mDownloadReceiver, filter, Context.RECEIVER_EXPORTED);
            } else {
                appContext.registerReceiver(mDownloadReceiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDownloadComplete(Context appContext, long downloadId) {
        if (downloadId != PrefsUtil.getLong(appContext, KEY_DOWNLOAD_ID, -1)) {
            return;
        }

        int status = queryDownloadStatus(appContext, downloadId);
        if (status == DownloadManager.STATUS_FAILED) {
            clearDownloadState(appContext);
            onDownloadFailed(appContext);
            return;
        }
        if (status != DownloadManager.STATUS_SUCCESSFUL) {
            return;
        }

        final String path = PrefsUtil.getString(appContext, KEY_DOWNLOAD_PATH, "");
        final String expectedMd5 = PrefsUtil.getString(appContext, KEY_DOWNLOAD_MD5, "");
        clearDownloadState(appContext);

        if (TextUtils.isEmpty(path)) {
            toast(appContext, R.string.update_install_failed);
            return;
        }

        final File apkFile = new File(path);
        if (!apkFile.exists()) {
            toast(appContext, R.string.update_install_failed);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(expectedMd5)
                        && !expectedMd5.equalsIgnoreCase(md5(apkFile))) {
                    //noinspection ResultOfMethodCallIgnored
                    apkFile.delete();
                    mHandler.post(() -> toast(appContext, R.string.update_download_corrupted));
                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mPendingInstallPath = apkFile.getAbsolutePath();
                        PrefsUtil.putString(appContext, KEY_PENDING_INSTALL_PATH,
                                mPendingInstallPath);

                        Activity activity = getActivity();
                        if (activity != null) {
                            installApk(activity, apkFile);
                        }
                    }
                });
            }
        }, "update-verify").start();
    }

    private int queryDownloadStatus(Context appContext, long downloadId) {
        DownloadManager downloadManager =
                (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            return DownloadManager.STATUS_FAILED;
        }

        Cursor cursor = null;
        try {
            cursor = downloadManager.query(
                    new DownloadManager.Query().setFilterById(downloadId));
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(
                        DownloadManager.COLUMN_STATUS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return DownloadManager.STATUS_FAILED;
    }

    private void installApk(Activity activity, File apkFile) {
        Context appContext = activity.getApplicationContext();
        if (!apkFile.exists()) {
            clearPendingInstall(appContext);
            toast(appContext, R.string.update_install_failed);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && !activity.getPackageManager().canRequestPackageInstalls()) {
            mPendingInstallPath = apkFile.getAbsolutePath();
            // 避免每次回到前台都自动跳转设置页
            PrefsUtil.remove(appContext, KEY_PENDING_INSTALL_PATH);
            toast(appContext, R.string.update_install_permission_tip);
            try {
                activity.startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + activity.getPackageName())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        clearPendingInstall(appContext);

        try {
            Uri apkUri = FileProvider.getUriForFile(appContext,
                    appContext.getPackageName() + ".fileprovider", apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, APK_MIME_TYPE);
            intent.setClipData(ClipData.newRawUri("", apkUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            toast(appContext, R.string.update_install_failed);
        }
    }

    /**
     * 下载失败提示：强制更新时提供重试或退出应用。
     */
    private void onDownloadFailed(Context appContext) {
        Activity activity = getActivity();
        if (activity == null
                || !PrefsUtil.getBoolean(appContext, KEY_FORCE_UPDATE_PENDING, false)) {
            toast(appContext, R.string.update_download_failed);
            return;
        }

        new AlertDialog.Builder(activity)
                .setTitle(R.string.update_download_failed_title)
                .setMessage(R.string.update_download_failed)
                .setCancelable(false)
                .setPositiveButton(R.string.retry,
                        (dialog, which) -> checkForUpdate(appContext, true))
                .setNegativeButton(R.string.exit_app,
                        (dialog, which) -> activity.finishAffinity())
                .show();
    }

    private void clearPendingInstall(Context appContext) {
        mPendingInstallPath = null;
        PrefsUtil.remove(appContext, KEY_PENDING_INSTALL_PATH);
    }

    private void clearDownloadState(Context appContext) {
        PrefsUtil.remove(appContext, KEY_DOWNLOAD_ID);
        PrefsUtil.remove(appContext, KEY_DOWNLOAD_PATH);
        PrefsUtil.remove(appContext, KEY_DOWNLOAD_MD5);
    }

    private File getUpdateDir(Context appContext) {
        File externalDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (externalDir == null) {
            return null;
        }
        File dir = new File(externalDir, UPDATE_DIR_NAME);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        return dir;
    }

    private void cleanOldApks(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".apk")) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    private String md5(File file) {
        InputStream inputStream = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, length);
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : digest.digest()) {
                builder.append(String.format(Locale.US, "%02x", b));
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                    // ignore
                }
            }
        }
    }

    private Activity getActivity() {
        Activity activity = mActivityRef == null ? null : mActivityRef.get();
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        return activity;
    }

    private void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}

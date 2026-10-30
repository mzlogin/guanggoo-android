package org.mazhuang.guanggoo.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mazhuang
 * @date 2017/11/18
 * code from https://www.ctolib.com/topics-110262.html
 */

public class FileUtils {

    private static final String APP_DIR_NAME = "out_photo";

    public static void saveImage(final Context context, final Bitmap bmp , final SaveResultCallback saveResultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置以当前时间格式为图片名称
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName = df.format(new Date()) + ".png";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 起分区存储生效，直接写外部存储目录会失败，改走 MediaStore
                    saveImageToMediaStore(context, bmp, fileName, saveResultCallback);
                } else {
                    saveImageToExternalStorage(context, bmp, fileName, saveResultCallback);
                }
            }
        }).start();
    }

    private static void saveImageToMediaStore(Context context, Bitmap bmp, String fileName,
                                              SaveResultCallback saveResultCallback) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/" + APP_DIR_NAME);
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        Uri uri = null;
        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri == null) {
                saveResultCallback.onSavedFailed();
                return;
            }
            OutputStream os = resolver.openOutputStream(uri);
            if (os == null) {
                throw new IOException("Failed to open output stream for " + uri);
            }
            try {
                if (!bmp.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                    throw new IOException("Failed to compress bitmap");
                }
            } finally {
                os.close();
            }

            ContentValues doneValues = new ContentValues();
            doneValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(uri, doneValues, null, null);

            saveResultCallback.onSavedSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            if (uri != null) {
                try {
                    resolver.delete(uri, null, null);
                } catch (Exception ignored) {
                    // nothing we can do here
                }
            }
            saveResultCallback.onSavedFailed();
        }
    }

    private static void saveImageToExternalStorage(Context context, Bitmap bmp, String fileName,
                                                   SaveResultCallback saveResultCallback) {
        if (bmp == null) {
            saveResultCallback.onSavedFailed();
            return;
        }

        File appDir = new File(Environment.getExternalStorageDirectory(), APP_DIR_NAME);
        if (!appDir.exists() && !appDir.mkdirs()) {
            saveResultCallback.onSavedFailed();
            return;
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            saveResultCallback.onSavedSuccess();
        } catch (Exception e) {
            saveResultCallback.onSavedFailed();
            e.printStackTrace();
            return;
        }

        // 通知相册刷新，Android 7 起不能再通过 file:// 广播（会抛 FileUriExposedException）
        try {
            MediaScannerConnection.scanFile(context.getApplicationContext(),
                    new String[]{file.getAbsolutePath()}, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface SaveResultCallback{
        /**
         * 保存成功
         */
        void onSavedSuccess();

        /**
         * 保存失败
         */
        void onSavedFailed();
    }
}

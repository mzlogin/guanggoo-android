package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.mazhuang.guanggoo.R;

/**
 *
 * @author mazhuang
 * @date 2017/10/6
 */

public class VersionUtil {

    private VersionUtil() {}

    /**
     * 获取 versionName
     */
    private static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.version_unknown);
        }
    }

    /**
     * 获取 versionCode
     */
    private static int getVersionCode(Context context) {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersion(Context context) {
        String versionName = getVersionName(context);
        int versionCode = getVersionCode(context);
        return context.getString(R.string.version_name_format,
                versionName,
                versionCode);
    }
}

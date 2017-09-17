package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mazhuang on 2017/5/19.
 */

public class PrefsUtil {
    private static final String CONFIG_FILE_NAME = "app_config";

    public static final String KEY_COOKIE = "cookie";

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPreferences(context).getLong(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }
}

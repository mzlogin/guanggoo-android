package org.mazhuang.guanggoo.imgchr;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;

import org.mazhuang.guanggoo.util.PrefsUtil;

/**
 * 路过图床账号登录态管理。
 *
 * @author mazhuang
 */
public class ImgchrAccount {

    private static final String KEY_COOKIE = "key_imgchr_cookie";

    private ImgchrAccount() {
    }

    public static String getCookie(Context context) {
        return PrefsUtil.getString(context, KEY_COOKIE, "");
    }

    public static void saveCookie(Context context, String cookie) {
        PrefsUtil.putString(context, KEY_COOKIE, cookie);
    }

    public static void clear(Context context) {
        PrefsUtil.remove(context, KEY_COOKIE);
    }

    public static boolean isLoggedIn(Context context) {
        return !TextUtils.isEmpty(getCookie(context));
    }

    /**
     * 清除 WebView 中路过图床的登录 Cookie。
     */
    public static void clearWebViewCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(ImgchrClient.getHomeUrl());
        if (TextUtils.isEmpty(cookie)) {
            return;
        }
        for (String part : cookie.split(";")) {
            int index = part.indexOf('=');
            if (index > 0) {
                String name = part.substring(0, index).trim();
                cookieManager.setCookie(ImgchrClient.getHomeUrl(),
                        name + "=; Max-Age=0; Path=/");
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
    }
}

package org.mazhuang.guanggoo.data;

import android.text.TextUtils;
import android.os.Build;
import android.webkit.CookieManager;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import java.util.Map;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class AuthInfoManager {

    private static final String KEY_USERNAME = "auth_username";
    private static final String KEY_AVATAR = "auth_avatar";

    private String username;

    private String avatar;

    private AuthInfoManager() {}

    private static class InstanceHolder {
        private static AuthInfoManager sInstance = new AuthInfoManager();
    }

    public static AuthInfoManager getInstance() {
        return InstanceHolder.sInstance;
    }

    public synchronized boolean isLoginIn() {
        restoreIfNeeded();
        return !TextUtils.isEmpty(username);
    }

    public synchronized String getUsername() {
        restoreIfNeeded();
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
        PrefsUtil.putString(App.getInstance(), KEY_USERNAME, username == null ? "" : username);
    }

    public synchronized String getAvatar() {
        restoreIfNeeded();
        return avatar;
    }

    public synchronized void setAvatar(String avatar) {
        this.avatar = avatar;
        PrefsUtil.putString(App.getInstance(), KEY_AVATAR, avatar == null ? "" : avatar);
    }

    private void restoreIfNeeded() {
        if (username == null) {
            username = PrefsUtil.getString(App.getInstance(), KEY_USERNAME, "");
        }
        if (avatar == null) {
            avatar = PrefsUtil.getString(App.getInstance(), KEY_AVATAR, "");
        }
    }

    public synchronized void clearAuthInfo() {
        PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_COOKIE, "");
        PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_XSRF, "");
        PrefsUtil.putString(App.getInstance(), KEY_USERNAME, "");
        PrefsUtil.putString(App.getInstance(), KEY_AVATAR, "");
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(ConstantUtil.BASE_URL, "user=; Max-Age=0; Path=/; Secure");
        cookieManager.setCookie(ConstantUtil.BASE_URL, "_xsrf=; Max-Age=0; Path=/; Secure");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
        username = null;
        avatar = null;
    }
}

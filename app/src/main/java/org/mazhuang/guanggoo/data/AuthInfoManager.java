package org.mazhuang.guanggoo.data;

import android.text.TextUtils;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import java.util.Map;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class AuthInfoManager {

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
        return !TextUtils.isEmpty(username);
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getAvatar() {
        return avatar;
    }

    public synchronized void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public synchronized void clearAuthInfo() {
        PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_COOKIE, "");
        PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_XSRF, "");
        username = null;
        avatar = null;
    }
}

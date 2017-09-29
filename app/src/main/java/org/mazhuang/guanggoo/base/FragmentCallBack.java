package org.mazhuang.guanggoo.base;

/**
 * Created by Lenovo on 2017/9/28.
 */

public interface FragmentCallBack {
    void openPage(String url, String title);
    void onLoginStatusChanged(boolean isLogin);
}

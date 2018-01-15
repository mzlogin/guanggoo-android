package org.mazhuang.guanggoo.base;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public interface FragmentCallBack {
    /**
     * 根据 url 打开标题为 title 的 Fragment
     * @param url 网页链接或自定义链接
     * @param title 页面标题
     */
    void openPage(String url, String title);

    /**
     * 登录状态发生变化时调用
     * @param isLogin 已登录为 true
     */
    void onLoginStatusChanged(boolean isLogin);

    /**
     * 显示 loading 动画
     */
    void startLoading();

    /**
     * 隐藏 loading 动画
     */
    void stopLoading();

    /**
     * loading 动画是否正在显示
     * @return 正在显示返回 true
     */
    boolean isLoading();
}

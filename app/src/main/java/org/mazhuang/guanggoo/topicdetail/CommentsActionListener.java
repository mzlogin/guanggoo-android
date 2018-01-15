package org.mazhuang.guanggoo.topicdetail;

/**
 *
 * @author mazhuang
 * @date 2017/10/6
 */

public interface CommentsActionListener {
    /**
     * 打开页面
     * @param url 网页链接或内部链接
     * @param title 页面标题
     */
    void openPage(String url, String title);

    /**
     * 艾特
     * @param username 要 @ 的用户名
     */
    void onAt(String username);
}

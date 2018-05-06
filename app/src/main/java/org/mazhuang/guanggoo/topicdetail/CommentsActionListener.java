package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.data.OnResponseListener;

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

    /**
     * 给评论点赞
     * @param username 要点赞的评论的作者
     * @param url 点赞 URL
     * @param listener 点赞结果回调
     */
    void onVote(String username, String url, OnResponseListener<Boolean> listener);
}

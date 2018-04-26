package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.TopicDetail;

/**
 *
 * @author mazhuang
 * @date 2017/9/17
 */

public interface TopicDetailContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取主题详情
         */
        void getTopicDetail();

        /**
         * 获取第 page 页的评论
         * @param page 页码
         */
        void getMoreComments(int page);

        /**
         * 发表新回复
         * @param content 回复内容
         */
        void comment(String content);

        /**
         * 收藏
         */
        void favorite();

        /**
         * 取消收藏
         */
        void unfavorite();

        /**
         * 对评论点赞
         * @param url URL
         * @param listener 点赞结果回调
         */
        void voteComment(String url, OnResponseListener<Boolean> listener);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取主题详情成功
         * @param topicDetail 主题详情
         */
        void onGetTopicDetailSucceed(TopicDetail topicDetail);

        /**
         * 获取主题详情失败
         * @param msg 失败提示信息
         */
        void onGetTopicDetailFailed(String msg);

        /**
         * 获取更多评论内容成功
         * @param topicDetail 包含更多评论内容的主题详情对象
         */
        void onGetMoreCommentsSucceed(TopicDetail topicDetail);

        /**
         * 获取更多评论内容失败
         * @param msg 失败提示信息
         */
        void onGetMoreCommentsFailed(String msg);

        /**
         * 发表评论成功
         */
        void onCommentSucceed();

        /**
         * 发表评论失败
         * @param msg 失败提示信息
         */
        void onCommentFailed(String msg);

        /**
         * 收藏成功
         */
        void onFavoriteSucceed();

        /**
         * 收藏失败
         * @param msg 失败提示信息
         */
        void onFavoriteFail(String msg);

        /**
         * 取消收藏成功
         */
        void onUnfavoriteSucceed();

        /**
         * 取消收藏失败
         * @param msg 失败提示信息
         */
        void onUnfavoriteFailed(String msg);

        /**
         * 评论点赞成功
         */
        void onVoteCommentSucceed();

        /**
         * 评论点赞失败
         * @param msg 失败提示信息
         */
        void onVoteCommentFailed(String msg);
    }
}

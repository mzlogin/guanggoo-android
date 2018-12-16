package org.mazhuang.guanggoo.userprofile.replies;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Reply;
/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public interface ReplyListContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取评论列表
         */
        void getReplyList();

        /**
         * 获取更多评论
         * @param page 第几页
         */
        void getMoreReply(int page);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取评论列表成功
         * @param replyList 评论内容
         */
        void onGetReplyListSucceed(ListResult<Reply> replyList);

        /**
         * 获取评论列表失败
         * @param msg 失败提示信息
         */
        void onGetReplyListFailed(String msg);

        /**
         * 获取更多评论成功
         * @param replyList 评论内容
         */
        void onGetMoreReplySucceed(ListResult<Reply> replyList);

        /**
         * 获取更多评论失败
         * @param msg 失败提示信息
         */
        void onGetMoreReplyFailed(String msg);
    }
}

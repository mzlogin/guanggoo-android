package org.mazhuang.guanggoo.userprofile.replies;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.ReplyList;
import org.mazhuang.guanggoo.data.entity.TopicList;

/**
 * Created by mazhuang on 2017/9/16.
 */

public interface ReplyListContract {
    interface Presenter extends BasePresenter {
        void getReplyList();
        void getMoreReply(int page);
    }

    interface View extends BaseView<Presenter> {
        void onGetReplyListSucceed(ReplyList replyList);
        void onGetReplyListFailed(String msg);

        void onGetMoreReplySucceed(ReplyList replyList);
        void onGetMoreReplyFailed(String msg);
    }
}

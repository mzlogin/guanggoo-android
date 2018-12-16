package org.mazhuang.guanggoo.userprofile.replies;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Reply;
import org.mazhuang.guanggoo.data.task.GetReplyListTask;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class ReplyListPresenter implements ReplyListContract.Presenter {

    private ReplyListContract.View mView;

    public ReplyListPresenter(ReplyListContract.View view) {
        mView = view;
        view.setPresenter(this);
    }

    @Override
    public void getReplyList() {
        NetworkTaskScheduler.getInstance().execute(new GetReplyListTask(mView.getUrl(),
                new OnResponseListener<ListResult<Reply>>() {
                    @Override
                    public void onSucceed(ListResult<Reply> data) {
                        mView.onGetReplyListSucceed(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetReplyListFailed(msg);
                    }
                }));
    }

    @Override
    public void getMoreReply(int page) {
        NetworkTaskScheduler.getInstance().execute(new GetReplyListTask(UrlUtil.appendPage(mView.getUrl(), page),
                new OnResponseListener<ListResult<Reply>>() {
                    @Override
                    public void onSucceed(ListResult<Reply> data) {
                        mView.onGetMoreReplySucceed(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetMoreReplyFailed(msg);
                    }
                }));
    }
}

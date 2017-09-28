package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.data.task.CommentTask;
import org.mazhuang.guanggoo.data.task.GetTopicDetailTask;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class TopicDetailPresenter implements TopicDetailContract.Presenter {

    private TopicDetailContract.View mView;

    public TopicDetailPresenter(TopicDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getTopicDetail() {
        NetworkTaskScheduler.getInstance().execute(new GetTopicDetailTask(getUrl(), new OnResponseListener<TopicDetail>() {
            @Override
            public void onSucceed(TopicDetail data) {
                mView.onGetTopicDetailSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.onGetTopicDetailFailed(msg);
            }
        }));
    }

    @Override
    public void getMoreComments(int page) {
        NetworkTaskScheduler.getInstance().execute(new GetTopicDetailTask(UrlUtil.appendPage(getUrl(), page), new OnResponseListener<TopicDetail>() {
            @Override
            public void onSucceed(TopicDetail data) {
                mView.onGetMoreCommentsSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.onGetMoreCommentsFailed(msg);
            }
        }));
    }

    @Override
    public void comment(String content) {
        NetworkTaskScheduler.getInstance().execute(new CommentTask(getUrl(), content, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                mView.onCommentSucceed();
            }

            @Override
            public void onFailed(String msg) {
                mView.onCommentFailed(msg);
            }
        }));
    }

    private String getUrl() {
        return mView.getUrl().replaceAll("#reply\\d+", "");
    }
}

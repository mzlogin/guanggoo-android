package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.data.task.GetTopicDetailTask;

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
    public void getTopicDetail(String url) {
        NetworkTaskScheduler.getInstance().execute(new GetTopicDetailTask(url, new OnResponseListener<TopicDetail>() {
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
}

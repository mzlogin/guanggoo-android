package org.mazhuang.guanggoo.topiclist;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.task.GetTopicTask;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.util.List;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class TopicListPresenter implements TopicListContract.Presenter {

    private TopicListContract.View mView;
    private String mUrl;

    public TopicListPresenter(TopicListContract.View view, String url) {
        mView = view;
        mUrl = url;
        view.setPresenter(this);
    }

    @Override
    public void getTopicList() {
        NetworkTaskScheduler.getInstance().execute(new GetTopicTask(mUrl,
                new OnResponseListener<List<Topic>>() {
                    @Override
                    public void onSucceed(List<Topic> data) {
                        mView.onGetTopicListSucceed(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetTopicListFailed(msg);
                    }
                }));
    }
}

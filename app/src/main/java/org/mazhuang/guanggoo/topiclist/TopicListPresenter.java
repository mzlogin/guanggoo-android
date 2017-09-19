package org.mazhuang.guanggoo.topiclist;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.task.GetTopicListTask;
import org.mazhuang.guanggoo.util.UrlUtil;

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
        NetworkTaskScheduler.getInstance().execute(new GetTopicListTask(mUrl,
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

    @Override
    public void getMoreTopic(int page) {
        NetworkTaskScheduler.getInstance().execute(new GetTopicListTask(UrlUtil.appendPage(mUrl, page),
                new OnResponseListener<List<Topic>>() {
                    @Override
                    public void onSucceed(List<Topic> data) {
                        mView.onGetMoreTopicSucceed(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetMoreTopicFailed(msg);
                    }
                }));
    }
}

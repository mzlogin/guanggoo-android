package org.mazhuang.guanggoo.topiclist;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicList;
import org.mazhuang.guanggoo.data.task.GetTopicListTask;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.util.List;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class TopicListPresenter implements TopicListContract.Presenter {

    private TopicListContract.View mView;

    public TopicListPresenter(TopicListContract.View view) {
        mView = view;
        view.setPresenter(this);
    }

    @Override
    public void getTopicList() {
        NetworkTaskScheduler.getInstance().execute(new GetTopicListTask(mView.getUrl(),
                new OnResponseListener<TopicList>() {
                    @Override
                    public void onSucceed(TopicList data) {
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
        NetworkTaskScheduler.getInstance().execute(new GetTopicListTask(UrlUtil.appendPage(mView.getUrl(), page),
                new OnResponseListener<TopicList>() {
                    @Override
                    public void onSucceed(TopicList data) {
                        mView.onGetMoreTopicSucceed(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetMoreTopicFailed(msg);
                    }
                }));
    }
}

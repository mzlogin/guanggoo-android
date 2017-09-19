package org.mazhuang.guanggoo.topiclist;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.Topic;

import java.util.List;

/**
 * Created by mazhuang on 2017/9/16.
 */

public interface TopicListContract {
    interface Presenter extends BasePresenter {
        void getTopicList();
        void getMoreTopic(int page);
    }

    interface View extends BaseView<Presenter> {
        void onGetTopicListSucceed(List<Topic> topicList);
        void onGetTopicListFailed(String msg);

        void onGetMoreTopicSucceed(List<Topic> topicList);
        void onGetMoreTopicFailed(String msg);
    }
}

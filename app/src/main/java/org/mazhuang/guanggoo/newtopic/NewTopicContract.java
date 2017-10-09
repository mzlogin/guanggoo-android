package org.mazhuang.guanggoo.newtopic;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;

/**
 * Created by mazhuang on 2017/10/10.
 */

public interface NewTopicContract {
    interface Presenter extends BasePresenter {
        void newTopic(String title, String content);
    }

    interface View extends BaseView<Presenter> {
        void onNewTopicSucceed();
        void onNewTopicFailed(String msg);
    }
}

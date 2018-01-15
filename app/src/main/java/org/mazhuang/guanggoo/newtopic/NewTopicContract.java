package org.mazhuang.guanggoo.newtopic;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;

/**
 *
 * @author mazhuang
 * @date 2017/10/10
 */

public interface NewTopicContract {
    interface Presenter extends BasePresenter {
        /**
         * 发表新主题
         * @param title 标题
         * @param content 内容
         */
        void newTopic(String title, String content);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 发表新主题成功
         */
        void onNewTopicSucceed();

        /**
         * 发表新主题失败
         * @param msg 失败提示信息
         */
        void onNewTopicFailed(String msg);
    }
}

package org.mazhuang.guanggoo.notifications;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Notification;

/**
 * @author mazhuang
 * @date 2018/8/18
 */
public interface NotificationsContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取消息提醒列表
         */
        void getNotificationList();

        /**
         * 获取更多消息提醒
         * @param page 第几页
         */
        void getMoreNotification(int page);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取消息提醒列表成功
         * @param notificationList 消息列表对象
         */
        void onGetNotificationListSucceed(ListResult<Notification> notificationList);

        /**
         * 获取消息提醒列表失败
         * @param msg 失败提示信息
         */
        void onGetNotificationListFailed(String msg);

        /**
         * 获取更多消息提醒成功
         * @param notificationList 消息提醒列表对象
         */
        void onGetMoreNotificationSucceed(ListResult<Notification> notificationList);

        /**
         * 获取更多消息提醒失败
         * @param msg 失败提示信息
         */
        void onGetMoreNotificationFailed(String msg);
    }
}

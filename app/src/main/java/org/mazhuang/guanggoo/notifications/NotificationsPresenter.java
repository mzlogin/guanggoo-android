package org.mazhuang.guanggoo.notifications;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Notification;
import org.mazhuang.guanggoo.data.task.BaseTask;
import org.mazhuang.guanggoo.data.task.GetNotificationListTask;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 * @author mazhuang
 * @date 2018/8/19
 */
public class NotificationsPresenter implements NotificationsContract.Presenter {

    private NotificationsContract.View mView;

    private BaseTask mCurrentTask;

    public NotificationsPresenter(NotificationsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getNotificationList() {
        if (mCurrentTask != null) {
            mCurrentTask.cancel();
        }

        mCurrentTask = new GetNotificationListTask(mView.getUrl(),
                new OnResponseListener<ListResult<Notification>>() {
                    @Override
                    public void onSucceed(ListResult<Notification> data) {
                        mView.onGetNotificationListSucceed(data);
                        mCurrentTask = null;
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetNotificationListFailed(msg);
                        mCurrentTask = null;
                    }
                });

        NetworkTaskScheduler.getInstance().execute(mCurrentTask);
    }

    @Override
    public void getMoreNotification(int page) {
        if (mCurrentTask != null) {
            mCurrentTask.cancel();
        }

        mCurrentTask = new GetNotificationListTask(UrlUtil.appendPage(mView.getUrl(), page),
                new OnResponseListener<ListResult<Notification>>() {
                    @Override
                    public void onSucceed(ListResult<Notification> data) {
                        mView.onGetMoreNotificationSucceed(data);
                        mCurrentTask = null;
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetMoreNotificationFailed(msg);
                        mCurrentTask = null;
                    }
                });

        NetworkTaskScheduler.getInstance().execute(mCurrentTask);
    }
}

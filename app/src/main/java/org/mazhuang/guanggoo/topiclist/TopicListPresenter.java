package org.mazhuang.guanggoo.topiclist;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.task.BaseTask;
import org.mazhuang.guanggoo.data.task.GetTopicListTask;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;


/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class TopicListPresenter implements TopicListContract.Presenter {

    private TopicListContract.View mView;

    private BaseTask mCurrentTask;

    private int mPagination = ConstantUtil.TOPICS_PER_PAGE;

    public TopicListPresenter(TopicListContract.View view) {
        mView = view;
        view.setPresenter(this);
    }

    public TopicListPresenter(TopicListContract.View view, int pagination) {
        this(view);
        mPagination = pagination;
    }

    @Override
    public void getTopicList() {
        if (mCurrentTask != null) {
            mCurrentTask.cancel();
        }

        mCurrentTask = new GetTopicListTask(mView.getUrl(),
                new OnResponseListener<ListResult<Topic>>() {
                    @Override
                    public void onSucceed(ListResult<Topic> data) {
                        mView.onGetTopicListSucceed(data);
                        mCurrentTask = null;
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetTopicListFailed(msg);
                        mCurrentTask = null;
                    }
                });

        NetworkTaskScheduler.getInstance().execute(mCurrentTask);
    }

    @Override
    public void getMoreTopic(int page) {
        if (mCurrentTask != null) {
            mCurrentTask.cancel();
        }

        mCurrentTask = new GetTopicListTask(UrlUtil.appendPage(mView.getUrl(), page),
                new OnResponseListener<ListResult<Topic>>() {
                    @Override
                    public void onSucceed(ListResult<Topic> data) {
                        mView.onGetMoreTopicSucceed(data);
                        mCurrentTask = null;
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.onGetMoreTopicFailed(msg);
                        mCurrentTask = null;
                    }
                });

        NetworkTaskScheduler.getInstance().execute(mCurrentTask);
    }

    @Override
    public int getPagination() {
        return mPagination;
    }
}

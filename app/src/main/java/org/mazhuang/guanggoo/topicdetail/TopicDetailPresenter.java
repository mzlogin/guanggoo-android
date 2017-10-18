package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Favorite;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.data.task.CommentTask;
import org.mazhuang.guanggoo.data.task.FavouriteTask;
import org.mazhuang.guanggoo.data.task.GetTopicDetailTask;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

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
    public void getTopicDetail() {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new GetTopicDetailTask(getUrl(), new OnResponseListener<TopicDetail>() {
            @Override
            public void onSucceed(TopicDetail data) {
                mView.stopLoading();
                mView.onGetTopicDetailSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onGetTopicDetailFailed(msg);
            }
        }));
    }

    @Override
    public void getMoreComments(int page) {
        NetworkTaskScheduler.getInstance().execute(new GetTopicDetailTask(UrlUtil.appendPage(getUrl(), page), new OnResponseListener<TopicDetail>() {
            @Override
            public void onSucceed(TopicDetail data) {
                mView.onGetMoreCommentsSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.onGetMoreCommentsFailed(msg);
            }
        }));
    }

    @Override
    public void comment(String content) {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new CommentTask(getUrl(), content, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                mView.stopLoading();
                mView.onCommentSucceed();
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onCommentFailed(msg);
            }
        }));
    }

    @Override
    public void favourite(final String favouriteState) {
        String topicId = UrlUtil.getTid(getUrl());
        String favouriteUrl = "";
        String nextState = "";
        if (Favorite.STATE_FAVORITE.equals(favouriteState)) {
            nextState = Favorite.STATE_UNFAVORITE;
            favouriteUrl = ConstantUtil.FAVORITE_URL + "?topic_id=" + topicId;
        } else if (Favorite.STATE_UNFAVORITE.equals(favouriteState)) {
            nextState = Favorite.STATE_FAVORITE;
            favouriteUrl = ConstantUtil.UN_FAVORITE_URL + "?topic_id=" + topicId;
        }

        final String finalNextState = nextState;
        NetworkTaskScheduler.getInstance().execute(new FavouriteTask(favouriteUrl, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                String msg = "";
                if (Favorite.STATE_FAVORITE.equals(favouriteState)) {
                    msg = "收藏成功";
                } else if (Favorite.STATE_UNFAVORITE.equals(favouriteState)) {
                    msg = "取消收藏成功";
                }
                mView.favouriteSuccess(msg, finalNextState);
            }

            @Override
            public void onFailed(String msg) {
                if (Favorite.STATE_FAVORITE.equals(favouriteState)) {
                    msg = "收藏失败";
                } else if (Favorite.STATE_UNFAVORITE.equals(favouriteState)) {
                    msg = "取消收藏失败";
                }
                mView.favouriteFail(msg);
            }
        }));
    }

    private String getUrl() {
        return mView.getUrl().replaceAll("#reply\\d+", "");
    }
}

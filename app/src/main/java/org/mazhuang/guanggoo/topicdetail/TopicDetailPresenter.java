package org.mazhuang.guanggoo.topicdetail;

import com.vdurmont.emoji.EmojiParser;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.data.task.CommentTask;
import org.mazhuang.guanggoo.data.task.FavouriteTask;
import org.mazhuang.guanggoo.data.task.GetTopicDetailTask;
import org.mazhuang.guanggoo.data.task.VoteCommentTask;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 *
 * @author mazhuang
 * @date 2017/9/17
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
        content = EmojiParser.parseToAliases(content);
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
    public void favorite() {
        String topicId = UrlUtil.getTid(getUrl());
        String favouriteUrl = ConstantUtil.FAVORITE_URL + "?topic_id=" + topicId;

        NetworkTaskScheduler.getInstance().execute(new FavouriteTask(favouriteUrl, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                mView.onFavoriteSucceed();
            }

            @Override
            public void onFailed(String msg) {
                mView.onFavoriteFail(msg);
            }
        }));
    }

    @Override
    public void unfavorite() {
        String topicId = UrlUtil.getTid(getUrl());
        String favouriteUrl = ConstantUtil.UN_FAVORITE_URL + "?topic_id=" + topicId;

        NetworkTaskScheduler.getInstance().execute(new FavouriteTask(favouriteUrl, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                mView.onUnfavoriteSucceed();
            }

            @Override
            public void onFailed(String msg) {
                mView.onUnfavoriteFailed(msg);
            }
        }));
    }

    @Override
    public void voteComment(String url, final OnResponseListener<Boolean> listener) {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new VoteCommentTask(url, new OnResponseListener<Boolean>() {
            @Override
            public void onSucceed(Boolean value) {
                mView.stopLoading();
                listener.onSucceed(value);
                if (value) {
                    mView.onVoteCommentSucceed();
                } else {
                    mView.onVoteCommentFailed("您已经点过赞了");
                }
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                listener.onFailed(msg);
                mView.onVoteCommentFailed(msg);
            }
        }));
    }

    private String getUrl() {
        return mView.getUrl().replaceAll("#reply\\d+", "");
    }
}

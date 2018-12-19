package org.mazhuang.guanggoo.search;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.data.task.GetUserProfileTask;

/**
 *
 * @author Lenovo
 * @date 2017/9/29
 */

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;

    public SearchPresenter(SearchContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getUserProfile(String url) {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new GetUserProfileTask(url, new OnResponseListener<UserProfile>() {
            @Override
            public void onSucceed(UserProfile data) {
                mView.stopLoading();
                mView.onGetUserProfileSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onGetUserProfileFailed(msg);
            }
        }));
    }
}

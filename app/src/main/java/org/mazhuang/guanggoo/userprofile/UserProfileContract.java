package org.mazhuang.guanggoo.userprofile;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.UserProfile;

/**
 * Created by Lenovo on 2017/9/29.
 */

public interface UserProfileContract {
    interface Presenter extends BasePresenter {
        void getUserProfile(String url);
    }

    interface View extends BaseView<Presenter> {
        void onGetUserProfileSucceed(UserProfile userProfile);
        void onGetUserProfileFailed(String msg);
    }
}

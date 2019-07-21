package org.mazhuang.guanggoo.userprofile;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.UserProfile;

/**
 *
 * @author Lenovo
 * @date 2017/9/29
 */

public interface UserProfileContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取用户信息
         * @param url 用户信息网址
         */
        void getUserProfile(String url);

        /**
         * 关注用户
         * @param username 用户名
         */
        void followUser(String username);

        /**
         * 取消关注
         * @param username 用户名
         */
        void unfollowUser(String username);

        /**
         * 屏蔽用户
         * @param profile 用户资料
         */
        void blockUser(UserProfile profile);

        /**
         * 取消屏蔽
         * @param profile 用户资料
         */
        void unblockUser(UserProfile profile);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取用户信息成功
         * @param userProfile 用户信息
         */
        void onGetUserProfileSucceed(UserProfile userProfile);

        /**
         * 获取用户信息失败
         * @param msg 失败提示信息
         */
        void onGetUserProfileFailed(String msg);

        /**
         * 关注用户成功
         */
        void onFollowUserSucceed();

        /**
         * 关注用户失败
         * @param msg 失败提示信息
         */
        void onFollowUserFailed(String msg);

        /**
         * 取消关注成功
         */
        void onUnfollowUserSucceed();

        /**
         * 取消关注失败
         * @param msg 失败提示信息
         */
        void onUnfollowUserFailed(String msg);

        /**
         * 屏蔽用户成功
         */
        void onBlockUserSucceed();

        /**
         * 屏蔽用户失败
         * @param msg 失败提示消息
         */
        void onBlockUserFailed(String msg);

        /**
         * 取消屏蔽成功
         */
        void onUnblockUserSucceed();

        /**
         * 取消屏蔽失败
         * @param msg 失败提示消息
         */
        void onUnblockUserFailed(String msg);
    }
}

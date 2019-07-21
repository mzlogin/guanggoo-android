package org.mazhuang.guanggoo.userprofile.block;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.UserProfile;

import java.util.List;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public interface BlockedUserListContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取已屏蔽用户列表
         */
        void getBlockedUserList();
    }


    interface View extends BaseView<Presenter> {
        /**
         * 获取已屏蔽用户列表成功
         * @param data 数据
         */
        void onGetBlockedUserListSucceed(List<UserProfile> data);

        /**
         * 获取已屏蔽用户列表失败
         * @param msg 失败提示信息
         */
        void onGetBlockedUserListFailed(String msg);
    }
}

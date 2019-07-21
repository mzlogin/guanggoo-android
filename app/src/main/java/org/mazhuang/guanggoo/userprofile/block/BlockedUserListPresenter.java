package org.mazhuang.guanggoo.userprofile.block;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.data.task.GetBlockedUserListTask;

import java.util.List;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public class BlockedUserListPresenter implements BlockedUserListContract.Presenter {

    private BlockedUserListContract.View mView;

    public BlockedUserListPresenter(BlockedUserListContract.View view) {
        mView = view;
        view.setPresenter(this);
    }

    @Override
    public void getBlockedUserList() {
        NetworkTaskScheduler.getInstance().execute(new GetBlockedUserListTask(new OnResponseListener<List<UserProfile>>() {
            @Override
            public void onSucceed(List<UserProfile> data) {
                mView.onGetBlockedUserListSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.onGetBlockedUserListFailed(msg);
            }
        }));
    }
}

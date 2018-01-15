package org.mazhuang.guanggoo.nodescloud;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.NodeCategory;
import org.mazhuang.guanggoo.data.task.GetNodesCloudTask;

import java.util.List;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public class NodesCloudPresenter implements NodesCloudContract.Presenter {

    private NodesCloudContract.View mView;

    public NodesCloudPresenter(NodesCloudContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getNodesCloud() {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new GetNodesCloudTask(new OnResponseListener<List<NodeCategory>>() {
            @Override
            public void onSucceed(List<NodeCategory> data) {
                mView.stopLoading();
                mView.onGetNodesCloudSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onGetNodesCloudFailed(msg);
            }
        }));
    }
}

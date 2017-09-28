package org.mazhuang.guanggoo.nodescloud;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.NodeCategory;

import java.util.List;

/**
 * Created by Lenovo on 2017/9/28.
 */

public interface NodesCloudContract {
    interface Presenter extends BasePresenter {
        void getNodesCloud();
    }

    interface View extends BaseView<Presenter> {
        void onGetNodesCloudSucceed(List<NodeCategory> nodesCloud);
        void onGetNodesCloudFailed(String msg);
    }
}

package org.mazhuang.guanggoo.nodescloud;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.NodeCategory;

import java.util.List;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public interface NodesCloudContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取所有节点
         */
        void getNodesCloud();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取节点成功
         * @param nodesCloud 节点分类对象
         */
        void onGetNodesCloudSucceed(List<NodeCategory> nodesCloud);

        /**
         * 获取节点失败
         * @param msg 失败提示信息
         */
        void onGetNodesCloudFailed(String msg);
    }
}

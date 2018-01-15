package org.mazhuang.guanggoo.nodescloud;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.NodeCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author mazhuang
 */
public class NodesCloudFragment extends BaseFragment<NodesCloudContract.Presenter>
        implements NodesCloudContract.View, OnNodeClickListener {

    @BindView(R.id.nodes_cloud) RecyclerView mNodesCloudListView;

    private NodesCloudAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nodes_cloud, container, false);

        ButterKnife.bind(this, root);

        initParams();

        initViews();

        if (mAdapter.getItemCount() == 0) {
            mPresenter.getNodesCloud();
        }

        return root;
    }

    private void initViews() {
        if (mAdapter == null) {
            mAdapter = new NodesCloudAdapter(mListener, this);
        }
        mNodesCloudListView.setAdapter(mAdapter);
    }

    @Override
    public void onGetNodesCloudSucceed(List<NodeCategory> nodesCloud) {
        if (getContext() == null) {
            return;
        }

        mAdapter.setData(nodesCloud);
    }

    @Override
    public String getTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return getString(R.string.nodes_list);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onGetNodesCloudFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNodeClick(Node node) {
        if (mListener != null) {
            mListener.openPage(node.getUrl(), node.getTitle());
        }
    }
}

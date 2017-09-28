package org.mazhuang.guanggoo.nodescloud;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

public class NodesCloudFragment extends BaseFragment<NodesCloudContract.Presenter> implements NodesCloudContract.View {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.nodes_cloud) RecyclerView mNodesCloudListView;

    private NodesCloudAdapter mAdapter;

    public NodesCloudFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nodes_cloud, container, false);

        ButterKnife.bind(this, root);

        initViews();

        mPresenter.getNodesCloud();

        return root;
    }

    private void initViews() {
        mAdapter = new NodesCloudAdapter(mListener);
        mNodesCloudListView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Node node);
    }

    @Override
    public void onGetNodesCloudSucceed(List<NodeCategory> nodesCloud) {
        mAdapter.setData(nodesCloud);
    }

    @Override
    public String getTitle() {
        return getString(R.string.nodes_list);
    }

    @Override
    public void onGetNodesCloudFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

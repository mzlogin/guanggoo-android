package org.mazhuang.guanggoo.userprofile.block;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.UserProfile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public class BlockedUserListFragment extends BaseFragment<BlockedUserListContract.Presenter> implements BlockedUserListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private BlockedUserListAdapter mAdapter;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty) SwipeRefreshLayout mEmptyLayout;

    private boolean mFirstFetchFinished = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blocked_user_list, container, false);

        ButterKnife.bind(this, root);

        if (mPresenter == null) {
            mPresenter = new BlockedUserListPresenter(this);
        }

        initViews();

        if (!mAdapter.isFilled()) {
            mPresenter.getBlockedUserList();
        }

        return root;
    }

    private void initViews() {
        Context context = getContext();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 1);
            }
        });
        if (mAdapter == null) {
            mAdapter = new BlockedUserListAdapter(mListener);
        }
        mRecyclerView.setAdapter(mAdapter);

        initSwipeLayout(mRefreshLayout);
        initSwipeLayout(mEmptyLayout);

        handleEmptyList();
    }

    private void initSwipeLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
    }

    private void handleEmptyList() {
        if (mAdapter.getItemCount() == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.blocked_user);
    }

    @Override
    public void onGetBlockedUserListSucceed(List<UserProfile> data) {
        finishRefresh();
        if (getContext() == null) {
            return;
        }
        mAdapter.setData(data);
        handleEmptyList();
    }

    @Override
    public void onGetBlockedUserListFailed(String msg) {
        finishRefresh();
        if (getContext() == null) {
            return;
        }
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        handleEmptyList();
    }

    @Override
    public void onRefresh() {
        if (!mFirstFetchFinished) {
            return;
        }

        mPresenter.getBlockedUserList();
    }

    private void finishRefresh() {
        if (mListener != null && mListener.isLoading()) {
            mListener.stopLoading();
        }

        mRefreshLayout.setRefreshing(false);
        mEmptyLayout.setRefreshing(false);
        if (!mFirstFetchFinished) {
            mFirstFetchFinished = true;
        }
    }
}

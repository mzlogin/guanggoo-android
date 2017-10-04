package org.mazhuang.guanggoo.topiclist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicList;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicListFragment extends BaseFragment<TopicListContract.Presenter> implements TopicListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    private TopicListAdapter mAdapter;
    private boolean mLoadable = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty) SwipeRefreshLayout mEmptyLayout;

    private boolean mFirstFetchFinished = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_list, container, false);

        initParams();

        ButterKnife.bind(this, root);

        initViews();

        if (!mAdapter.isFilled()) {
            mPresenter.getTopicList();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mFirstFetchFinished) {
            mRefreshLayout.setRefreshing(true);
        }
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
            mAdapter = new TopicListAdapter(mListener);
        }
        mRecyclerView.setAdapter(mAdapter);

        // ref https://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (mLoadable) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            mLoadable = false;
                            if (totalItemCount >= ConstantUtil.TOPICS_PER_PAGE && totalItemCount <= 1024) {
                                mPresenter.getMoreTopic(totalItemCount / ConstantUtil.TOPICS_PER_PAGE + 1);
                            } else {
                                Toast.makeText(getActivity(), "1024", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(this);
        mEmptyLayout.setOnRefreshListener(this);

        handleEmptyList();
    }

    @Override
    public void onGetTopicListSucceed(TopicList topicList) {

        finishRefresh();

        if (getContext() == null) {
            return;
        }

        mLoadable = topicList.isHasMore();

        mAdapter.setData(topicList.getTopics());

        handleEmptyList();
    }

    @Override
    public void onGetTopicListFailed(String msg) {

        finishRefresh();

        if (getContext() == null) {
            return;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

        handleEmptyList();
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
        if (TextUtils.isEmpty(mTitle)) {
            return getString(R.string.topic_list);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onGetMoreTopicSucceed(TopicList topicList) {
        if (getContext() == null) {
            return;
        }

        mLoadable = topicList.isHasMore();

        mAdapter.addData(topicList.getTopics());
    }

    @Override
    public void onGetMoreTopicFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        if (!mFirstFetchFinished) {
            return;
        }

        mPresenter.getTopicList();
    }

    private void finishRefresh() {
        mRefreshLayout.setRefreshing(false);
        mEmptyLayout.setRefreshing(false);
        if (!mFirstFetchFinished) {
            mFirstFetchFinished = true;
        }
    }
}

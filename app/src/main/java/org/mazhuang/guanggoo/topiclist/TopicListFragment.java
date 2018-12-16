package org.mazhuang.guanggoo.topiclist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.router.FragmentFactory;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author mazhuang
 */
public class TopicListFragment extends BaseFragment<TopicListContract.Presenter> implements TopicListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    private TopicListAdapter mAdapter;
    private boolean mLoadable = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty) SwipeRefreshLayout mEmptyLayout;
    @BindView(R.id.fab) CircleImageView mFabButton;
    @BindView(R.id.no_content) TextView mNoContentTextView;

    private boolean mFirstFetchFinished = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_list, container, false);

        ButterKnife.bind(this, root);

        initViews();

        if (!mAdapter.isFilled()) {
            mPresenter.getTopicList();
        }

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mFirstFetchFinished && mListener != null) {
            mRefreshLayout.setRefreshing(true);
            mEmptyLayout.setRefreshing(true);
        }
    }

    private void initViews() {
        Context context = getContext();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        if (mAdapter == null) {
            mAdapter = new TopicListAdapter(mListener);
        }
        mRecyclerView.setAdapter(mAdapter);

        // ref https://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //check for scroll down
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (mLoadable) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            mLoadable = false;

                            int pagination = mPresenter.getPagination();

                            if (totalItemCount >= pagination && totalItemCount <= ConstantUtil.MAX_TOPICS) {
                                mPresenter.getMoreTopic(totalItemCount / pagination + 1);
                            } else {
                                Toast.makeText(getActivity(), "1024", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        initSwipeLayout(mRefreshLayout);
        initSwipeLayout(mEmptyLayout);

        handleEmptyList();

        handleFabButton();
    }

    private void handleFabButton() {
        if (getPageType() == FragmentFactory.PageType.HOME_TOPIC_LIST ||
                getPageType() == FragmentFactory.PageType.NODE_TOPIC_LIST) {
            mFabButton.setVisibility(View.VISIBLE);
        }
    }

    private void initSwipeLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                onNewTopic();
                break;

            default:
                break;
        }
    }

    private void onNewTopic() {
        if (mPageType == FragmentFactory.PageType.HOME_TOPIC_LIST) {
            if (mListener != null) {
                mListener.openPage(ConstantUtil.SELECT_NODE_URL, getString(R.string.select_node_to_new_topic));
            }
        } else if (mPageType == FragmentFactory.PageType.NODE_TOPIC_LIST) {
            String nodeCode = UrlUtil.getNodeCode(mUrl);
            if (mListener != null && !TextUtils.isEmpty(nodeCode)) {
                mListener.openPage(String.format(ConstantUtil.NEW_TOPIC_BASE_URL, nodeCode), getString(R.string.new_topic));
            }
        }
    }

    @Override
    public void onGetTopicListSucceed(ListResult<Topic> topicList) {

        finishRefresh();

        if (getContext() == null) {
            return;
        }

        if (topicList.getData().isEmpty()) {
            mNoContentTextView.setText(R.string.no_content);
        }

        mLoadable = topicList.isHasMore();

        mAdapter.setData(topicList.getData());

        handleEmptyList();
    }

    @Override
    public void onGetTopicListFailed(String msg) {

        finishRefresh();

        if (getContext() == null) {
            return;
        }

        mNoContentTextView.setText(R.string.no_content);

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
    public void onGetMoreTopicSucceed(ListResult<Topic> topicList) {
        if (getContext() == null) {
            return;
        }

        mLoadable = topicList.isHasMore();

        mAdapter.addData(topicList.getData());
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

        mNoContentTextView.setText("");

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

package org.mazhuang.guanggoo.userprofile.replies;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Reply;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author mazhuang
 */
public class ReplyListFragment extends BaseFragment<ReplyListContract.Presenter> implements ReplyListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    private ReplyListAdapter mAdapter;
    private boolean mLoadable = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty) SwipeRefreshLayout mEmptyLayout;

    private boolean mFirstFetchFinished = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reply_list, container, false);

        initParams();

        ButterKnife.bind(this, root);

        if (mPresenter == null) {
            mPresenter = new ReplyListPresenter(this);
        }

        initViews();

        if (!mAdapter.isFilled()) {
            mPresenter.getReplyList();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mFirstFetchFinished && mListener != null) {
            mListener.startLoading();
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
            mAdapter = new ReplyListAdapter(mListener);
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
                            if (totalItemCount >= ConstantUtil.REPLIES_PER_PAGE && totalItemCount <= ConstantUtil.MAX_TOPICS) {
                                mPresenter.getMoreReply(totalItemCount / ConstantUtil.REPLIES_PER_PAGE + 1);
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
    }

    private void initSwipeLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
    }

    @Override
    public void onGetReplyListSucceed(ListResult<Reply> replyList) {

        finishRefresh();

        if (getContext() == null) {
            return;
        }

        mLoadable = replyList.isHasMore();

        mAdapter.setData(replyList.getData());

        handleEmptyList();
    }

    @Override
    public void onGetReplyListFailed(String msg) {

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
            return getString(R.string.reply_list);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onGetMoreReplySucceed(ListResult<Reply> replyList) {
        if (getContext() == null) {
            return;
        }

        mLoadable = replyList.isHasMore();

        mAdapter.addData(replyList.getData());
    }

    @Override
    public void onGetMoreReplyFailed(String msg) {
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

        mPresenter.getReplyList();
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

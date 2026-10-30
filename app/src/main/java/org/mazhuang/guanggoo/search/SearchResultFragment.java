package org.mazhuang.guanggoo.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.SearchResult;
import org.mazhuang.guanggoo.data.entity.SearchResultPage;
import org.mazhuang.guanggoo.data.task.ResolveUrlTask;
import org.mazhuang.guanggoo.util.UrlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子搜索结果页。
 *
 * @author mazhuang
 */
public class SearchResultFragment extends BaseFragment<SearchResultContract.Presenter>
        implements SearchResultContract.View, SearchResultAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SearchResultAdapter mAdapter;
    private String mNextPageUrl;
    private boolean mLoadable = false;
    private boolean mLoadingMore = false;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty) SwipeRefreshLayout mEmptyLayout;
    @BindView(R.id.no_content) TextView mNoContentTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);

        ButterKnife.bind(this, root);

        if (mPresenter == null) {
            mPresenter = new SearchResultPresenter(this);
        }

        initViews();

        if (!mAdapter.isFilled()) {
            if (TextUtils.isEmpty(mUrl)) {
                mNoContentTextView.setText(R.string.search_no_result);
                handleEmptyList();
            } else {
                mEmptyLayout.setRefreshing(true);
                mPresenter.search(mUrl);
            }
        }

        return root;
    }

    private void initViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchResultAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0 || !mLoadable || mLoadingMore) {
                    return;
                }

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    loadMore();
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.main);
        mEmptyLayout.setOnRefreshListener(this);
        mEmptyLayout.setColorSchemeResources(R.color.main);

        handleEmptyList();
    }

    private void loadMore() {
        if (TextUtils.isEmpty(mNextPageUrl)) {
            mLoadable = false;
            return;
        }

        mLoadable = false;
        mLoadingMore = true;
        mPresenter.loadMore(mNextPageUrl);
    }

    @Override
    public void onRefresh() {
        mLoadable = false;
        mLoadingMore = false;
        mNextPageUrl = null;

        if (TextUtils.isEmpty(mUrl)) {
            finishRefresh();
            return;
        }

        mPresenter.search(mUrl);
    }

    @Override
    public void onSearchSucceed(SearchResultPage page, boolean append) {
        finishRefresh();
        mLoadingMore = false;

        if (getContext() == null) {
            return;
        }

        if (append) {
            mAdapter.addData(page.getData());
        } else {
            if (page.getData().isEmpty()) {
                mNoContentTextView.setText(R.string.search_no_result);
            } else {
                mNoContentTextView.setText("");
            }
            mAdapter.setData(page.getData());
        }

        mNextPageUrl = page.getNextPageUrl();
        mLoadable = !TextUtils.isEmpty(mNextPageUrl);

        handleEmptyList();
    }

    @Override
    public void onSearchFailed(String msg, boolean append) {
        finishRefresh();
        mLoadingMore = false;

        if (getContext() == null) {
            return;
        }

        toast(msg);

        // 加载更多失败时保留下一页地址，方便再次下拉到底部重试
        mLoadable = append && !TextUtils.isEmpty(mNextPageUrl);

        handleEmptyList();
    }

    private void finishRefresh() {
        mRefreshLayout.setRefreshing(false);
        mEmptyLayout.setRefreshing(false);
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
    public void onItemClick(SearchResult result) {
        String url = result.getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (url.contains("/search/reply/")) {
            // 回复命中链接会重定向到对应主题，先解析出主题地址
            resolveAndOpen(url);
        } else {
            openTopic(url);
        }
    }

    private void resolveAndOpen(String url) {
        startLoading();

        NetworkTaskScheduler.getInstance().execute(new ResolveUrlTask(url,
                new OnResponseListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        stopLoading();
                        openTopic(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        stopLoading();
                        toast(msg);
                    }
                }));
    }

    private void openTopic(String url) {
        if (mListener == null) {
            return;
        }
        mListener.openPage(UrlUtil.removeQuery(url), null);
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        }
        return getString(R.string.search_results);
    }
}

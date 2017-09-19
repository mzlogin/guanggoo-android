package org.mazhuang.guanggoo.topicdetail;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.TopicDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class TopicDetailFragment extends BaseFragment<TopicDetailContract.Presenter> implements TopicDetailContract.View {

    public static final String KEY_URL = "url";

    private String mUrl;
    private TopicDetail mTopicDetail;
    private CommentsListAdapter mAdapter;

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.last_touched) TextView mLastTouchedTextView;
    @BindView(R.id.author) TextView mAuthorTextView;
    @BindView(R.id.node) TextView mNodeTextView;

    @BindView(R.id.content) WebView mContentWebView;

    @BindView(R.id.comments) RecyclerView mCommentsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        ButterKnife.bind(this, root);

        initParams();

        initWebView();

        initRecyclerView();

        mPresenter.getTopicDetail(mUrl);

        return root;
    }

    private void initRecyclerView() {
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCommentsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 1, 0, 0);
            }
        });
        if (mAdapter == null) {
            mAdapter = new CommentsListAdapter(mListener);
        }
        mCommentsRecyclerView.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCommentsRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        if (mContentWebView != null) {
            mContentWebView.loadUrl("about:blank");
            mContentWebView = null;
        }
        super.onDestroy();
    }

    private void initWebView() {
        mContentWebView.getSettings().setUseWideViewPort(false);
        mContentWebView.getSettings().setLoadWithOverviewMode(true);
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mContentWebView.setWebChromeClient(new WebChromeClient());
        mContentWebView.setWebViewClient(new WebViewClient());
        mContentWebView.getSettings().setJavaScriptEnabled(true);
    }

    private void initParams() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(KEY_URL);
        }
    }

    @Override
    public void onGetTopicDetailSucceed(TopicDetail topicDetail) {
        mTopicDetail = topicDetail;

        mTitleTextView.setText(topicDetail.getTopic().getTitle());
        Glide.with(getContext())
                .load(topicDetail.getTopic().getAvatar())
                .centerCrop()
                .crossFade()
                .into(mAvatarImageView);
        mLastTouchedTextView.setText(topicDetail.getTopic().getMeta().getLastTouched());
        mAuthorTextView.setText(topicDetail.getTopic().getMeta().getAuthor().getUsername());
        mNodeTextView.setText(topicDetail.getTopic().getMeta().getNode().getTitle());

        // 相比 loadData，这个调用能解决中文乱码的问题
        mContentWebView.loadDataWithBaseURL(null, topicDetail.getContent() + "<style>img{display:inline; height:auto; max-width:100%;} a{word-break:break-all; word-wrap:break-word;} pre, code, pre code{word-wrap:normal; overflow:auto;} pre{padding:16px; bordor-radius:3px; border:1px solid #ccc;}</style>", "text/html", "UTF-8", null);

        mAdapter.setData(mTopicDetail.getComments());
    }

    @Override
    public void onGetTopicDetailFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        return "主题详情";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment item);
    }
}

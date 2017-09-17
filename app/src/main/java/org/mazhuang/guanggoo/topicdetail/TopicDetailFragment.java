package org.mazhuang.guanggoo.topicdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.util.MyHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class TopicDetailFragment extends BaseFragment<TopicDetailContract.Presenter> implements TopicDetailContract.View {

    public static final String KEY_URL = "url";

    private String mUrl;
    private TopicDetail mTopicDetail;

    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.last_touched) TextView mLastTouchedTextView;
    @BindView(R.id.author) TextView mAuthorTextView;
    @BindView(R.id.node) TextView mNodeTextView;

    @BindView(R.id.content) WebView mContentTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        ButterKnife.bind(this, root);

        initParams();

        initWebView();

        mPresenter.getTopicDetail(mUrl);

        return root;
    }

    @Override
    public void onDestroy() {
        if (mContentTextView != null) {
            mContentTextView.loadUrl("about:blank");
            mContentTextView = null;
        }
        super.onDestroy();
    }

    private void initWebView() {
        mContentTextView.getSettings().setUseWideViewPort(false);
        mContentTextView.getSettings().setLoadWithOverviewMode(true);
        mContentTextView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mContentTextView.setWebChromeClient(new WebChromeClient());
        mContentTextView.setWebViewClient(new WebViewClient());
        mContentTextView.getSettings().setJavaScriptEnabled(true);
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
        mContentTextView.loadDataWithBaseURL(null, topicDetail.getContent() + "<style>img{display:inline; height:auto; max-width:100%;} a{word-break:break-all; word-wrap:break-word;} pre, code, pre code{word-wrap:normal; overflow:auto;} pre{padding:16px; bordor-radius:3px; border:1px solid #ccc;}</style>", "text/html", "UTF-8", null);
    }

    @Override
    public void onGetTopicDetailFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        return "主题详情";
    }
}

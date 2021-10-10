package org.mazhuang.guanggoo.topicdetail;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseUploadImageFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.router.FragmentFactory;
import org.mazhuang.guanggoo.ui.widget.PreImeEditText;
import org.mazhuang.guanggoo.util.*;

import java.io.InputStream;

/**
 *
 * @author mazhuang
 * @date 2017/9/17
 */

public class TopicDetailFragment extends BaseUploadImageFragment<TopicDetailContract.Presenter> implements TopicDetailContract.View, CommentsActionListener {

    private TopicDetail mTopicDetail;
    private CommentsListAdapter mAdapter;

    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.created_time) TextView mCreatedTimeTExtView;
    @BindView(R.id.author) TextView mAuthorTextView;
    @BindView(R.id.node) TextView mNodeTextView;
    @BindView(R.id.content) WebView mContentWebView;
    @BindView(R.id.comments_count) TextView mCommentsCountTextView;
    @BindView(R.id.comments) RecyclerView mCommentsRecyclerView;
    @BindView(R.id.comment) PreImeEditText mCommentEditText;
    @BindView(R.id.submit) Button mSubmitButton;
    @BindView(R.id.comment_view) View mCommentsView;
    @BindView(R.id.favorite) ImageView mFavoriteImageView;
    @BindView(R.id.follow) Button mFollowButton;
    @BindView(R.id.load_more) TextView mLoadMoreTextView;

    private View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            return mRoot;
        }

        mRoot = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        ButterKnife.bind(this, mRoot);

        if (mPresenter == null) {
            mPresenter = new TopicDetailPresenter(this);
        }

        initParams();

        initViews();

        if (mTopicDetail == null) {
            mPresenter.getTopicDetail();
        } else {
            setViewData(mTopicDetail);
        }

        return mRoot;
    }

    private void setViewData(TopicDetail topicDetail) {
        setFavoriteState(topicDetail.getFavorite().isFavorite());
        mTitleTextView.setText(topicDetail.getTopic().getTitle());
        GlideUtil.loadImage(mAvatarImageView, topicDetail.getTopic().getAvatar());
        mCreatedTimeTExtView.setText(topicDetail.getTopic().getMeta().getCreatedTime());
        mAuthorTextView.setText(topicDetail.getTopic().getMeta().getAuthor().getUsername());
        mNodeTextView.setText(topicDetail.getTopic().getMeta().getNode().getTitle());

        int lineHeight = 22;
        try {
            float lineHeightOrigin = 18.5714f * getResources().getDimension(R.dimen.line_spacing_multiplier);
            lineHeight = Math.round(lineHeightOrigin);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        String style = new StringBuilder()
                .append("<style>html,body{padding:0;margin:0;} .ui-content{margin:0;padding:0 18px 0 18px;font-size:15px; line-height:")
                .append(lineHeight)
                .append("px;} img{display:inline; height:auto; max-width:100%;} a{word-break:break-all; word-wrap:break-word;} pre, code, pre code{word-wrap:normal; overflow:auto;} pre{padding:16px; bordor-radius:3px; border:1px solid #ccc;}</style>")
                .toString();

        // 相比 loadData，这个调用能解决中文乱码的问题
        mContentWebView.loadDataWithBaseURL(null, topicDetail.getContent() + style, "text/html", "UTF-8", null);

        mAdapter.setData(mTopicDetail.getComments());

        if (mAdapter.getItemCount() < mTopicDetail.getCommentsCount()) {
            mLoadMoreTextView.setVisibility(View.VISIBLE);
            mLoadMoreTextView.setEnabled(true);
        }

        mCommentsCountTextView.setText(getString(R.string.comments_count, mTopicDetail.getCommentsCount()));

        if (topicDetail.getTopic().getMeta().getAuthor().isFollowed()) {
            mFollowButton.setText(R.string.unfollow);
        }
    }

    private boolean isSelfOwnTopic() {
        return AuthInfoManager.getInstance().getUsername().equals(mTopicDetail.getTopic().getMeta().getAuthor().getUsername());
    }

    private void initViews() {
        initWebView();
        initRecyclerView();

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSubmitButton.setEnabled(!TextUtils.isEmpty(editable));
            }
        });
        mCommentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SoftInputUtil.showSoftInput(getActivity());
                } else {
                    SoftInputUtil.hideSoftInputFromWindow(getActivity(), v);
                }
            }
        });
        mCommentEditText.setParentView(mCommentsView);
    }

    @OnClick({R.id.load_more, R.id.submit, R.id.author, R.id.avatar,
            R.id.node, R.id.edit, R.id.edit_text, R.id.favorite, R.id.share,
    R.id.follow, R.id.add_image})
    public void onClick(View v) {
        if (mTopicDetail == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.load_more: {
                mLoadMoreTextView.setEnabled(false);
                if (mAdapter.getItemCount() >= mTopicDetail.getCommentsCount()) {
                    mLoadMoreTextView.setVisibility(View.GONE);
                    return;
                }
                int page;
                if (PrefsUtil.getBoolean(App.getInstance(), ConstantUtil.KEY_COMMENTS_ORDER_DESC, false)) {
                    page = (mTopicDetail.getCommentsCount() - mTopicDetail.getComments().size()) / ConstantUtil.COMMENTS_PER_PAGE;
                } else {
                    page = mTopicDetail.getComments().size() / ConstantUtil.COMMENTS_PER_PAGE + 1;
                }
                mPresenter.getMoreComments(page);
            }
                break;

            case R.id.submit:
                if (mListener != null && mListener.isLoading()) {
                    return;
                }

                Boolean telephoneVerified = App.getInstance().mGlobal.telephoneVerified.getValue();

                if (!Boolean.TRUE.equals(telephoneVerified)) {
                    alertTelephoneVerify();
                    return;
                }

                if (TextUtils.isEmpty(mCommentEditText.getText())) {
                    Toast.makeText(getContext(), R.string.please_input_comment_content, Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.comment(mCommentEditText.getText().toString());
                }
                break;

            case R.id.author:
            case R.id.avatar:
                if (mListener != null) {
                    mListener.openPage(mTopicDetail.getTopic().getMeta().getAuthor().getUrl(), null);
                }
                break;

            case R.id.node:
                if (mListener != null) {
                    Node node = mTopicDetail.getTopic().getMeta().getNode();
                    mListener.openPage(node.getUrl(), node.getTitle());
                }
                break;

            case R.id.edit:
            case R.id.edit_text:
                showCommentView();
                break;

            case R.id.favorite:
                if (!AuthInfoManager.getInstance().isLoginIn()) {
                    toast(getString(R.string.please_login_first));
                    return;
                }

                if (isSelfOwnTopic()) {
                    toast(getString(R.string.cannot_favorite_self_own_topic));
                    return;
                }
                if (mTopicDetail.getFavorite().isFavorite()) {
                    mPresenter.unfavorite();
                } else {
                    mPresenter.favorite();
                }
                break;

            case R.id.share:
                ShareUtil.shareLink(getActivity(), mUrl);
                break;

            case R.id.follow: {
                if (!AuthInfoManager.getInstance().isLoginIn()) {
                    toast(getString(R.string.please_login_first));
                    return;
                }

                if (isSelfOwnTopic()) {
                    toast(getString(R.string.cannot_follow_self));
                    return;
                }
                String username = mTopicDetail.getTopic().getMeta().getAuthor().getUsername();
                if (getString(R.string.follow).equals(mFollowButton.getText().toString())) {
                    mPresenter.followUser(username);
                } else {
                    mPresenter.unfollowUser(username);
                }
                break;
            }

            case R.id.add_image:
                chooseImage();
                break;

            default:
                break;
        }
    }

    @Override
    public void doUploadImage(InputStream inputStream) {
        mPresenter.uploadImage(inputStream);
    }

    @OnLongClick({R.id.author, R.id.avatar})
    public boolean onLongClick(View v) {
        if (mTopicDetail == null) {
            return false;
        }

        switch (v.getId()) {
            case R.id.avatar:
            case R.id.author:
                onAt(mTopicDetail.getTopic().getMeta().getAuthor().getUsername());
                return true;

            default:
                break;
        }

        return false;
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCommentsRecyclerView.setLayoutManager(layoutManager);

        mCommentsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 1);
            }
        });
        if (mAdapter == null) {
            mAdapter = new CommentsListAdapter(this);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContentWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mContentWebView, true);
        }
        mContentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = UrlUtil.removeQuery(url);

                FragmentFactory.PageType pageType = FragmentFactory.getPageTypeByUrl(url);
                if (mListener != null) {
                    if (pageType != FragmentFactory.PageType.NONE) {
                        mListener.openPage(url, null);
                        return true;
                    }
                }

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        });
        mContentWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onGetTopicDetailSucceed(TopicDetail topicDetail) {

        if (getContext() == null) {
            return;
        }

        mTopicDetail = topicDetail;

        setViewData(mTopicDetail);
    }

    @Override
    public void onGetTopicDetailFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMoreCommentsSucceed(TopicDetail topicDetail) {
        mAdapter.addData(topicDetail.getComments());
        if (mAdapter.getItemCount() >= mTopicDetail.getCommentsCount()) {
            mLoadMoreTextView.setVisibility(View.GONE);
        } else {
            mLoadMoreTextView.setEnabled(true);
        }
    }

    @Override
    public void onGetMoreCommentsFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        mLoadMoreTextView.setEnabled(true);
    }

    @Override
    public String getTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return getString(R.string.topic_detail);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onCommentSucceed() {
        mCommentEditText.setText("");
        mCommentsView.setVisibility(View.GONE);
        mPresenter.getTopicDetail();
    }

    @Override
    public void onCommentFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setFavoriteState(boolean favorite) {
        mTopicDetail.getFavorite().setFavorite(favorite);
        if (favorite) {
            mFavoriteImageView.setImageResource(R.drawable.ic_menu_favors);
        } else {
            mFavoriteImageView.setImageResource(R.drawable.unfavorite);
        }
    }

    public void showCommentView() {
        mCommentEditText.requestFocus();
        mCommentsView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBackPressed() {
        if (mCommentsView.getVisibility() == View.VISIBLE) {
            mCommentsView.setVisibility(View.GONE);
            return true;
        }

        return super.onBackPressed();
    }

    @Override
    public void openPage(String url, String title) {
        if (mListener != null) {
            mListener.openPage(url, title);
        }
    }

    @Override
    public void onAt(String username) {
        if (!TextUtils.isEmpty(username)) {
            if (mCommentsView.getVisibility() != View.VISIBLE) {
                showCommentView();
            }

            String atText = String.format(" @%s ", username);
            if (!mCommentEditText.getText().toString().contains(atText)) {
                mCommentEditText.setText(String.format("%s%s", mCommentEditText.getText(), atText));
            }
            mCommentEditText.setSelection(mCommentEditText.getText().length());
        }
    }

    @Override
    public void onVote(String username, String url, OnResponseListener<Boolean> listener) {
        if (AuthInfoManager.getInstance().getUsername().equals(username)) {
            toast(getString(R.string.cannot_vote_self));
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        mPresenter.voteComment(url, listener);
    }

    @Override
    public void onFavoriteSucceed() {
        if (getContext() == null) {
            return;
        }

        boolean favorite = mTopicDetail.getFavorite().isFavorite();
        setFavoriteState(!favorite);
        toast(getString(R.string.favorite_succeed));
    }

    @Override
    public void onFavoriteFail(String msg) {
        toast(msg);
    }

    @Override
    public void onUnfavoriteSucceed() {
        if (getContext() == null) {
            return;
        }

        boolean favorite = mTopicDetail.getFavorite().isFavorite();
        setFavoriteState(!favorite);

        toast(getString(R.string.unfavorite_succeed));
    }

    @Override
    public void onUnfavoriteFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onVoteCommentSucceed() {

    }

    @Override
    public void onVoteCommentFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onFollowUserSucceed() {
        toast("关注成功");
        mFollowButton.setText(R.string.unfollow);
    }

    @Override
    public void onFollowUserFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onUnfollowUserSucceed() {
        toast("取消关注成功");
        mFollowButton.setText(R.string.follow);
    }

    @Override
    public void onUnfollowUserFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onUploadImageSucceed(String url) {
        if (!TextUtils.isEmpty(url)) {
            String imageMarkdown = String.format("[![](%s)](%s)", url, url);
            String text = TextUtils.isEmpty(mCommentEditText.getText()) ?
                    imageMarkdown :
                    String.format("%s\n%s", mCommentEditText.getText(), imageMarkdown);
            mCommentEditText.setText(text);
            mCommentEditText.setSelection(mCommentEditText.getText().length());
        }
    }

    @Override
    public void onUploadImageFailed(String msg) {
        toast(msg);
    }
}

package org.mazhuang.guanggoo.topicdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.MainActivity;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.sufficientlysecure.htmltextview.ClickableTableSpan;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
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

    @BindView(R.id.content) HtmlTextView mContentTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        ButterKnife.bind(this, root);

        initParams();

        mPresenter.getTopicDetail(mUrl);

        return root;
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

        mContentTextView.setHtml(topicDetail.getContent(), new HtmlHttpImageGetter(mContentTextView));
    }

    @Override
    public void onGetTopicDetailFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

package org.mazhuang.guanggoo.userprofile;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mazhuang
 */
public class UserProfileFragment extends BaseFragment<UserProfileContract.Presenter> implements UserProfileContract.View {

    private UserProfile mUserProfile;

    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.username) TextView mUsernameTextView;
    @BindView(R.id.number) TextView mNumberTextView;
    @BindView(R.id.logout) TextView mLogoutTextView;
    @BindView(R.id.title_favorite) TextView mFavoriteTextView;
    @BindView(R.id.title_topic) TextView mTopicTextView;
    @BindView(R.id.title_reply) TextView mReplyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, root);

        initParams();

        if (mUserProfile == null) {
            mPresenter.getUserProfile(mUrl);
        } else {
            setViewData(mUserProfile);
        }

        return root;
    }

    @Override
    public void initParams() {
        super.initParams();

        if (ConstantUtil.USER_PROFILE_SELF_FAKE_URL.equals(mUrl)) {
            // 处理登录后进入自己页面的情况
            mUrl = String.format(ConstantUtil.USER_PROFILE_BASE_URL, AuthInfoManager.getInstance().getUsername());

            mLogoutTextView.setVisibility(View.VISIBLE);
            mFavoriteTextView.setText(R.string.my_favorite);
            mTopicTextView.setText(R.string.my_topic);
            mReplyTextView.setText(R.string.my_reply);
        }
    }

    @Override
    public void onGetUserProfileSucceed(UserProfile userProfile) {
        if (getContext() == null) {
            return;
        }

        mUserProfile = userProfile;
        setViewData(mUserProfile);
    }

    private void setViewData(UserProfile userProfile) {
        mUsernameTextView.setText(userProfile.getUsername());
        Glide.with(getContext())
                .load(userProfile.getAvatar())
                .centerCrop()
                .crossFade()
                .into(mAvatarImageView);
        mNumberTextView.setText(userProfile.getNumber());
    }

    @Override
    public void onGetUserProfileFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        } else {
            return getString(R.string.profile);
        }
    }

    @OnClick({R.id.user_favors, R.id.user_topics, R.id.user_replies, R.id.logout})
    public void onClick(View v) {
        if (mListener == null || mUserProfile == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.user_favors:
                mListener.openPage(String.format(ConstantUtil.USER_FAVORS_BASE_URL, mUserProfile.getUsername()),
                        getString(R.string.user_favors));
                break;

            case R.id.user_topics:
                mListener.openPage(String.format(ConstantUtil.USER_TOPICS_BASE_URL, mUserProfile.getUsername()),
                        getString(R.string.user_topics));
                break;

            case R.id.user_replies:
                mListener.openPage(String.format(ConstantUtil.USER_REPLIES_BASE_URL, mUserProfile.getUsername()),
                        getString(R.string.user_replies));
                break;

            case R.id.logout:
                mListener.onLoginStatusChanged(false);
                getActivity().onBackPressed();
                break;

            default:
                break;
        }
    }
}

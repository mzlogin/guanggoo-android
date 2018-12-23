package org.mazhuang.guanggoo.userprofile;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.GlideApp;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.ui.widget.MenuItemBadge;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.GlideUtil;

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
    @BindView(R.id.follow) TextView mFollowTextView;
    @BindView(R.id.title_favorite) TextView mFavoriteTextView;
    @BindView(R.id.title_topic) TextView mTopicTextView;
    @BindView(R.id.title_reply) TextView mReplyTextView;

    private boolean mIsToViewSelfProfile = false;

    private View mRoot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot != null) {
            return mRoot;
        }

        mRoot = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, mRoot);

        initViews();

        if (mUserProfile == null) {
            mPresenter.getUserProfile(mUrl);
        } else {
            setViewData(mUserProfile);
        }

        return mRoot;
    }

    public void initViews() {
        // 是否是进自己个人中心的明确意图
        mIsToViewSelfProfile = ConstantUtil.USER_PROFILE_SELF_FAKE_URL.equals(mUrl);

        // 是否已登录且意图查看自己资料
        boolean isSelf = (AuthInfoManager.getInstance().isLoginIn() &&
                        String.format(ConstantUtil.USER_PROFILE_BASE_URL, AuthInfoManager.getInstance().getUsername()).equals(mUrl));

        if (mIsToViewSelfProfile) {
            mLogoutTextView.setVisibility(View.VISIBLE);
        }

        if (mIsToViewSelfProfile || isSelf) {
            // 处理登录后进入自己页面的情况
            mUrl = String.format(ConstantUtil.USER_PROFILE_BASE_URL, AuthInfoManager.getInstance().getUsername());

            mFavoriteTextView.setText(R.string.my_favorite);
            mTopicTextView.setText(R.string.my_topic);
            mReplyTextView.setText(R.string.my_reply);
        } else if (AuthInfoManager.getInstance().isLoginIn()) {
            // 已登录，打开的不是自己的页面
            mFollowTextView.setVisibility(View.VISIBLE);
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
        GlideUtil.loadImage(mAvatarImageView, userProfile.getAvatar());
        mNumberTextView.setText(userProfile.getNumber());
        mFollowTextView.setText(
                getString(userProfile.isFollowed() ? R.string.unfollow : R.string.follow));
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

    @OnClick({R.id.user_favors, R.id.user_topics, R.id.user_replies, R.id.logout, R.id.follow})
    public void onClick(View v) {
        if (mListener == null || mUserProfile == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.user_favors:
                mListener.openPage(String.format(ConstantUtil.USER_FAVORS_BASE_URL, mUserProfile.getUsername()),
                        mFavoriteTextView.getText().toString());
                break;

            case R.id.user_topics:
                mListener.openPage(String.format(ConstantUtil.USER_TOPICS_BASE_URL, mUserProfile.getUsername()),
                        mTopicTextView.getText().toString());
                break;

            case R.id.user_replies:
                mListener.openPage(String.format(ConstantUtil.USER_REPLIES_BASE_URL, mUserProfile.getUsername()),
                        mReplyTextView.getText().toString());
                break;

            case R.id.logout:
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_AlertDialog))
                        .setTitle(R.string.logout_confirm)
                        .setMessage(R.string.logout_tip_message)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onLoginStatusChanged(false);
                                getActivity().onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
                break;

            case R.id.follow: {
                String username = mUserProfile.getUsername();
                if (!mUserProfile.isFollowed()) {
                    mPresenter.followUser(username);
                } else {
                    mPresenter.unfollowUser(username);
                }
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onFollowUserSucceed() {
        if (getContext() == null) {
            return;
        }

        mUserProfile.setFollowed(true);

        toast("关注成功");
        mFollowTextView.setText(R.string.unfollow);
    }

    @Override
    public void onFollowUserFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onUnfollowUserSucceed() {
        if (getContext() == null) {
            return;
        }

        mUserProfile.setFollowed(false);

        toast("取消关注成功");
        mFollowTextView.setText(R.string.follow);
    }

    @Override
    public void onUnfollowUserFailed(String msg) {
        toast(msg);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mIsToViewSelfProfile && AuthInfoManager.getInstance().isLoginIn()) {
            inflater.inflate(R.menu.user_profile, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mListener.openPage(ConstantUtil.SETTINGS_URL, null);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

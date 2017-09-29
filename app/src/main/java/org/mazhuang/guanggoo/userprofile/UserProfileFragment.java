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
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends BaseFragment<UserProfileContract.Presenter> implements UserProfileContract.View {

    private UserProfile mUserProfile;

    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.username) TextView mUsernameTextView;
    @BindView(R.id.number) TextView mNumberTextView;
    @BindView(R.id.since) TextView mSinceTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initParams();

        ButterKnife.bind(this, root);

        if (mUserProfile == null) {
            mPresenter.getUserProfile(mUrl);
        } else {
            setViewData(mUserProfile);
        }

        return root;
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
        mSinceTextView.setText(userProfile.getSince());
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

    @OnClick({R.id.user_favors})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_favors:
                if (mListener != null) {
                    mListener.openPage(String.format(ConstantUtil.USER_FAVORS_BASE_URL, mUserProfile.getUsername()),
                            getString(R.string.user_favors));
                }
                break;

            default:
                break;
        }
    }
}

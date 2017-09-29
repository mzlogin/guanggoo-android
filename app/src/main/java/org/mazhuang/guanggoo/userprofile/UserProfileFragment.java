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

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileFragment extends BaseFragment<UserProfileContract.Presenter> implements UserProfileContract.View {

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

        mPresenter.getUserProfile(mUrl);

        return root;
    }

    @Override
    public void onGetUserProfileSucceed(UserProfile userProfile) {
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
}

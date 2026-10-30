package org.mazhuang.guanggoo.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.imgchr.ImgchrAccount;
import org.mazhuang.guanggoo.imgchr.ImgchrLoginActivity;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.DialogUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @author mazhuang
 * @date 2018/12/23
 */
public class SettingsFragment extends BaseFragment {

    private static final int REQUEST_CODE_LOGIN = 400;

    @BindView(R.id.comments_order_desc) SwitchCompat mCommentsOrderDescSwitch;
    @BindView(R.id.imgchr_account_status) TextView mImgchrAccountStatusTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, root);

        initParams();

        initViews();

        return root;
    }

    private void initViews() {
        boolean commentsOrderDesc = PrefsUtil.getBoolean(App.getInstance(), ConstantUtil.KEY_COMMENTS_ORDER_DESC, false);
        mCommentsOrderDescSwitch.setChecked(commentsOrderDesc);

        updateImgchrAccountStatus();
    }

    private void updateImgchrAccountStatus() {
        boolean loggedIn = ImgchrAccount.isLoggedIn(App.getInstance());
        mImgchrAccountStatusTextView.setText(loggedIn
                ? R.string.imgchr_logged_in : R.string.imgchr_not_logged_in);
    }

    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }

    @OnCheckedChanged(R.id.comments_order_desc)
    public void onCommentsOrderDescChanged(CompoundButton button, boolean value) {
        PrefsUtil.putBoolean(App.getInstance(), ConstantUtil.KEY_COMMENTS_ORDER_DESC, value);
    }

    @OnClick(R.id.imgchr_account)
    public void onImgchrAccountClicked() {
        if (getActivity() == null) {
            return;
        }

        if (!ImgchrAccount.isLoggedIn(App.getInstance())) {
            openLoginPage();
            return;
        }

        DialogUtil.builder(getActivity())
                .setItems(new CharSequence[]{
                        getString(R.string.imgchr_relogin),
                        getString(R.string.imgchr_logout)
                }, (dialog, which) -> {
                    if (which == 0) {
                        openLoginPage();
                    } else {
                        ImgchrAccount.clear(App.getInstance());
                        ImgchrAccount.clearWebViewCookies();
                        updateImgchrAccountStatus();
                    }
                })
                .show();
    }

    private void openLoginPage() {
        if (getActivity() != null) {
            startActivityForResult(new Intent(getActivity(), ImgchrLoginActivity.class),
                    REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                updateImgchrAccountStatus();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

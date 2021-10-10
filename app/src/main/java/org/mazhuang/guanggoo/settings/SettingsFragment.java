package org.mazhuang.guanggoo.settings;

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
import org.mazhuang.guanggoo.util.ConstantUtil;
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

    @BindView(R.id.comments_order_desc) SwitchCompat mCommentsOrderDescSwitch;
    @BindView(R.id.imgbb_label) TextView mImgbbLabelTextView;
    @BindView(R.id.imgbb_content) TextView mImgbbContentTextView;

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

        String imgBbApiKey = PrefsUtil.getString(App.getInstance(), ConstantUtil.KEY_IMGBB_API_KEY, "");
        mImgbbContentTextView.setText(imgBbApiKey);
    }

    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }

    @OnCheckedChanged(R.id.comments_order_desc)
    public void onCommentsOrderDescChanged(CompoundButton button, boolean value) {
        PrefsUtil.putBoolean(App.getInstance(), ConstantUtil.KEY_COMMENTS_ORDER_DESC, value);
    }

    @OnClick({R.id.imgbb_api_key})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbb_api_key: {
                Bundle bundle = new Bundle();
                bundle.putString(SettingsEditFragment.SETTINGS_KEY, ConstantUtil.KEY_IMGBB_API_KEY);
                bundle.putString(SettingsEditFragment.SETTINGS_HINT, getString(R.string.imgbb_api_key_hint));
                mListener.openPage(ConstantUtil.SETTINGS_EDIT_URL, mImgbbLabelTextView.getText().toString(), bundle);
            }
                break;
            default:
                break;
        }
    }
}

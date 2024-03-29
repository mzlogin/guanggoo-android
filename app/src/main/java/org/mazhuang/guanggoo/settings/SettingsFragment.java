package org.mazhuang.guanggoo.settings;

import android.content.Intent;
import android.net.Uri;
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
    @BindView(R.id.img_bed_label) TextView mImgBedLabelTextView;
    @BindView(R.id.img_bed_content) TextView mImgBedContentTextView;

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

        String imgBedApiKey = PrefsUtil.getString(App.getInstance(), ConstantUtil.KEY_IMG_BED_API_KEY, "");
        mImgBedContentTextView.setText(imgBedApiKey);
    }

    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }

    @OnCheckedChanged(R.id.comments_order_desc)
    public void onCommentsOrderDescChanged(CompoundButton button, boolean value) {
        PrefsUtil.putBoolean(App.getInstance(), ConstantUtil.KEY_COMMENTS_ORDER_DESC, value);
    }

    @OnClick({R.id.img_bed_api_key, R.id.help_image_bed_api_key})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_bed_api_key: {
                Bundle bundle = new Bundle();
                bundle.putString(SettingsEditFragment.SETTINGS_KEY, ConstantUtil.KEY_IMG_BED_API_KEY);
                bundle.putString(SettingsEditFragment.SETTINGS_HINT, getString(R.string.img_bed_api_key_hint));
                mListener.openPage(ConstantUtil.SETTINGS_EDIT_URL, mImgBedLabelTextView.getText().toString(), bundle);
            }
                break;

            case R.id.help_image_bed_api_key:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantUtil.IMAGE_BED_HELP_URL)));
                break;

            default:
                break;
        }
    }
}

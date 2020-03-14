package org.mazhuang.guanggoo.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.tencent.bugly.beta.Beta;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.ShareUtil;
import org.mazhuang.guanggoo.util.VersionUtil;

/**
 *
 * @author mazhuang
 * @date 2017/10/6
 */

public class AboutFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Element checkUpdateElement = new Element();
        checkUpdateElement.setTitle(getString(R.string.check_updates))
                .setIconDrawable(R.drawable.ic_update)
                .setOnClickListener(v -> Beta.checkUpgrade(true, false));

        Element qrcodeElement = new Element();
        qrcodeElement.setTitle(getString(R.string.feedback))
                .setIconDrawable(R.drawable.ic_feedback)
                .setOnClickListener(v -> mListener.openPage(ConstantUtil.FEEDBACK_URL, getString(R.string.feedback)));

        Element shareElement = new Element();
        shareElement.setTitle(getString(R.string.share_to_friend))
                .setIconDrawable(R.drawable.ic_menu_share)
                .setOnClickListener(v -> {
                    if (getActivity() != null) {
                        ShareUtil.shareLink(getActivity(), ConstantUtil.DOWNLOAD_URL_COOLAPK);
                    }
                });

        Element policyElement = new Element();
        policyElement.setTitle(getString(R.string.privacy_and_policy))
                .setIconDrawable(R.drawable.ic_policy)
                .setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantUtil.PRIVACY_URL))));

        return new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.guanggoo_new)
                .setDescription(getString(R.string.app_description, VersionUtil.getVersion(getContext())))
                .addItem(checkUpdateElement)
                .addItem(qrcodeElement)
                .addItem(shareElement)
                .addItem(policyElement)
                .addGitHub(getString(R.string.source_code_address), getString(R.string.source_code))
                .create();
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        } else {
            return getString(R.string.about);
        }
    }
}

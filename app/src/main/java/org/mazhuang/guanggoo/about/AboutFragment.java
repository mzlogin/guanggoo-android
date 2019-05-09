package org.mazhuang.guanggoo.about;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.bugly.beta.Beta;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.VersionUtil;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

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
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Beta.checkUpgrade(true, false);
                    }
                });

        Element qrcodeElement = new Element();
        qrcodeElement.setTitle(getString(R.string.qrcode))
                .setIconDrawable(R.drawable.ic_apps)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.openPage(ConstantUtil.QRCODE_URL, getString(R.string.qrcode));
                    }
                });

        return new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.guanggoo_new)
                .setDescription(getString(R.string.app_description, VersionUtil.getVersion(getContext())))
                // .addWebsite(getString(R.string.issue_address), getString(R.string.feedback))
                .addItem(checkUpdateElement)
                .addItem(qrcodeElement)
                // .addGroup(getString(R.string.contact_me))
                .addEmail(getString(R.string.author_email), getString(R.string.contact_me))
                // .addWebsite(getString(R.string.author_website), getString(R.string.author_website))
                // .addGitHub(getString(R.string.author_github), getString(R.string.follow_github))
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

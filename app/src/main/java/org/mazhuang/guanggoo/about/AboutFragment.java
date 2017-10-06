package org.mazhuang.guanggoo.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.VersionUtil;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by mazhuang on 2017/10/6.
 */

public class AboutFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Element versionElement = new Element();
        versionElement.setTitle(VersionUtil.getVersion(getContext()));

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.guanggoo_new)
                .setDescription(getString(R.string.app_description))
                .addItem(versionElement)
                .addGroup(getString(R.string.contact_me))
                .addEmail(getString(R.string.author_email), getString(R.string.author_email))
                .addWebsite(getString(R.string.author_website), getString(R.string.author_website))
                .addGitHub(getString(R.string.author_github), getString(R.string.follow_github))
                .create();

        return aboutPage;
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

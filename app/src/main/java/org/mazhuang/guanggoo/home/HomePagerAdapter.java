package org.mazhuang.guanggoo.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.router.FragmentFactory;
import org.mazhuang.guanggoo.util.ConstantUtil;

/**
 *
 * @author mazhuang
 * @date 2017/12/23
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private static int sPageCount;
    private static String[] sTitles;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
        sTitles = App.getInstance().getResources().getStringArray(R.array.home_tab_names);
        sPageCount = sTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;

        String url = null;

        switch (position) {
            case 0:
                url = ConstantUtil.BASE_URL;
                break;

            case 1:
                url = ConstantUtil.LATEST_URL;
                break;

            case 2:
                url = ConstantUtil.ELITE_URL;
                break;

            default:
                break;
        }

        if (!TextUtils.isEmpty(url)) {
            fragment = FragmentFactory.getFragmentByUrl(url);
            Bundle bundle = new Bundle();
            bundle.putString(BaseFragment.KEY_URL, url);
            bundle.putString(BaseFragment.KEY_TITLE, getPageTitle(position).toString());
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return sPageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }
}

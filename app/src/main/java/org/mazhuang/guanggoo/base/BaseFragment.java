package org.mazhuang.guanggoo.base;

import android.support.v4.app.Fragment;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class BaseFragment<T> extends Fragment {
    protected T mPresenter;

    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    public boolean onBackPressed() {
        return false;
    }
}

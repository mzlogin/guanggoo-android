package org.mazhuang.guanggoo.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mazhuang on 2017/9/16.
 */

public abstract class BaseFragment<T> extends Fragment {
    protected T mPresenter;

    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    public boolean onBackPressed() {
        return false;
    }

    public abstract String getTitle();
}

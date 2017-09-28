package org.mazhuang.guanggoo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by mazhuang on 2017/9/16.
 */

public abstract class BaseFragment<T> extends Fragment {
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";

    protected T mPresenter;
    protected FragmentCallBack mListener;

    protected String mUrl;
    protected String mTitle;

    protected boolean mClearTop = false;

    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void initParams() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(KEY_URL);
            mTitle = bundle.getString(KEY_TITLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallBack) {
            mListener = (FragmentCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public abstract String getTitle();

    public boolean isClearTop() {
        return mClearTop;
    }

    public void setClearTop(boolean mClearTop) {
        this.mClearTop = mClearTop;
    }

    public String getUrl() {
        return mUrl;
    }
}

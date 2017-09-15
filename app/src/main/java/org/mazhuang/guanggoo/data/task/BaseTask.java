package org.mazhuang.guanggoo.data.task;

import android.os.Handler;
import android.os.Looper;

import org.mazhuang.guanggoo.data.OnResponseListener;

/**
 * Created by mazhuang on 2017/9/16.
 */

public abstract class BaseTask<T> implements Runnable {
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected OnResponseListener<T> mListener;

    BaseTask(OnResponseListener<T> listener) {
        mListener = listener;
    }

    protected void successOnUI(final T data) {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSucceed(data);
                }
            });
        }
    }

    protected void failedOnUI(final String msg) {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailed(msg);
                }
            });
        }
    }
}

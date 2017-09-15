package org.mazhuang.guanggoo.data;

/**
 * Created by mazhuang on 2017/9/16.
 */

public interface OnResponseListener<T> {
    void onSucceed(T data);
    void onFailed(String msg);
}

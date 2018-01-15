package org.mazhuang.guanggoo.data;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public interface OnResponseListener<T> {
    /**
     * 任务执行成功
     * @param data 回调的数据
     */
    void onSucceed(T data);

    /**
     * 任务执行失败
     * @param msg 失败提示信息
     */
    void onFailed(String msg);
}

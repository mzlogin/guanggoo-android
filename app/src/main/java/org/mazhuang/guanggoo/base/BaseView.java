package org.mazhuang.guanggoo.base;

/**
 * Created by mazhuang on 2017/9/16.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    String getUrl();
}

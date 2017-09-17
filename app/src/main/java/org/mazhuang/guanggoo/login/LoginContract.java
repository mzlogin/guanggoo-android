package org.mazhuang.guanggoo.login;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;

import java.util.Map;

/**
 * Created by mazhuang on 2017/9/16.
 */

public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login(String email, String password);
    }

    interface View extends BaseView<Presenter> {
        void onLoginSucceed(String data);
        void onLoginFailed(String msg);
    }
}

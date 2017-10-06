package org.mazhuang.guanggoo.login;

import android.net.Network;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.task.LoginTask;

import java.util.Map;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        view.setPresenter(this);
    }

    @Override
    public void login(String email, String password) {
        mView.startLoading();
        NetworkTaskScheduler.getInstance().execute(new LoginTask(email, password, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                mView.stopLoading();
                mView.onLoginSucceed(data);
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onLoginFailed(msg);
            }
        }));
    }
}

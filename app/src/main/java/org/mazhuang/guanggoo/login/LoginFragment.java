package org.mazhuang.guanggoo.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment<LoginContract.Presenter> implements LoginContract.View {

    @BindView(R.id.email) EditText mEmailEdit;
    @BindView(R.id.password) EditText mPasswordEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, root);

        initParams();

        return root;
    }

    @OnClick({R.id.login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                attemptLogin();
                break;

            default:
                break;
        }
    }

    private void attemptLogin() {
        mEmailEdit.setError(null);
        mPasswordEdit.setError(null);

        String email = mEmailEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEdit.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEdit;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailEdit.setError(getString(R.string.error_field_required));
            focusView = mEmailEdit;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailEdit.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mPresenter.login(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Override
    public void onLoginSucceed(String data) {
        getActivity().onBackPressed();
        if (mListener != null && !ConstantUtil.LOGIN_URL.equals(mUrl)) {
            mListener.openPage(mUrl, mTitle);
        }
    }

    @Override
    public void onLoginFailed(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        return getString(R.string.login);
    }
}

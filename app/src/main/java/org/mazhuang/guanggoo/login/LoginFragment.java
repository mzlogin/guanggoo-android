package org.mazhuang.guanggoo.login;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Uses the site's real login page so its browser security checks can run. Once
 * signed in, only cookies and basic user information are copied back; all other
 * screens continue to use the native UI and networking implementation.
 */
public class LoginFragment extends BaseFragment<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.login_web_view) WebView mWebView;

    private boolean mLoginHandled;
    private int mLoginProbeCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        initParams();
        initWebView();
        return root;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !isGuozaokeUrl(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !isGuozaokeUrl(request.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tryCompleteLogin(url);
            }
        });

        mWebView.loadUrl(ConstantUtil.LOGIN_URL);
        // The redesigned site can complete login via AJAX without changing
        // the URL or firing another page-finished callback.
        mWebView.postDelayed(() -> probeLoginPage(), 600);
    }

    private void probeLoginPage() {
        if (mLoginHandled || mWebView == null || mLoginProbeCount++ >= 40) {
            return;
        }
        tryCompleteLogin(mWebView.getUrl());
        if (!mLoginHandled) {
            mWebView.postDelayed(() -> probeLoginPage(), 500);
        }
    }

    private boolean isGuozaokeUrl(String url) {
        return url != null && (url.equals(ConstantUtil.BASE_URL)
                || url.startsWith(ConstantUtil.BASE_URL + "/"));
    }

    private void tryCompleteLogin(String url) {
        if (mLoginHandled) {
            return;
        }

        String cookieHeader = CookieManager.getInstance().getCookie(ConstantUtil.BASE_URL);
        if (TextUtils.isEmpty(cookieHeader)) {
            return;
        }

        JSONObject cookies = new JSONObject();
        try {
            for (String item : cookieHeader.split(";")) {
                String cookie = item.trim();
                int separator = cookie.indexOf('=');
                if (separator <= 0) {
                    continue;
                }
                cookies.put(cookie.substring(0, separator), cookie.substring(separator + 1));
            }

            if (!cookies.has("user")) {
                return;
            }

            PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_COOKIE, cookies.toString());
            if (cookies.has(ConstantUtil.KEY_XSRF)) {
                PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_XSRF,
                        cookies.optString(ConstantUtil.KEY_XSRF));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().flush();
            }
        } catch (JSONException e) {
            onLoginFailed(getString(R.string.error_happened));
            return;
        }

        readUserInfoFromPage();
    }

    private void readUserInfoFromPage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || mWebView == null || mLoginHandled) {
            onLoginFailed(getString(R.string.error_happened));
            return;
        }

        String script = "(function(){"
                + "var c=document.querySelector('div.usercard');"
                + "if(!c)return null;"
                + "var u=c.querySelector('div.username');"
                + "var link=c.querySelector(\"a[href^='/u/']\");"
                + "var a=c.querySelector('img.avatar');"
                + "var name=u?u.textContent.trim():'';"
                + "if(!name&&link){var href=link.getAttribute('href');name=href.substring(3).split(/[?#/]/)[0];}"
                + "return JSON.stringify({username:name,avatar:a?a.src:''});"
                + "})()";
        mWebView.evaluateJavascript(script, value -> {
            try {
                if (!TextUtils.isEmpty(value) && !"null".equals(value)) {
                    String decoded = new JSONObject("{\"value\":" + value + "}").getString("value");
                    JSONObject user = new JSONObject(decoded);
                    String username = user.optString("username", "");
                    if (TextUtils.isEmpty(username)) {
                        retryUserInfo();
                        return;
                    }
                    mLoginHandled = true;
                    AuthInfoManager.getInstance().setUsername(username);
                    AuthInfoManager.getInstance().setAvatar(user.optString("avatar", ""));
                    onLoginSucceed("");
                    return;
                }
            } catch (JSONException e) {
                retryUserInfo();
                return;
            }
            retryUserInfo();
        });
    }

    private void retryUserInfo() {
        if (!mLoginHandled && mWebView != null) {
            mWebView.postDelayed(() -> {
                if (!mLoginHandled && mWebView != null) {
                    readUserInfoFromPage();
                }
            }, 500);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onLoginSucceed(String data) {
        if (getContext() == null || mListener == null) {
            return;
        }
        mListener.onLoginStatusChanged(true);
        if (getFragmentManager() != null) {
            // Remove the WebView fragment synchronously before opening the
            // pending native page; otherwise the async pop can win the
            // transaction race and leave the user on the login screen.
            getFragmentManager().popBackStackImmediate();
        }
        if (!ConstantUtil.LOGIN_URL.equals(mUrl)) {
            mListener.openPage(mUrl, mTitle);
        }
    }

    @Override
    public void onLoginFailed(String msg) {
        toast(msg);
    }

    @Override
    public String getTitle() {
        return getString(R.string.login);
    }
}

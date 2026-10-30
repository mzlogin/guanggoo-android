package org.mazhuang.guanggoo.imgchr;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import org.mazhuang.guanggoo.R;

/**
 * 路过图床登录页，登录成功后保存 Cookie 供后续上传使用。
 *
 * @author mazhuang
 */
public class ImgchrLoginActivity extends AppCompatActivity {

    /**
     * Android 15 起 targetSdk 35+ 的应用强制全屏显示（edge-to-edge）。
     */
    private static final int SDK_VERSION_ANDROID_15 = 35;

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private boolean mChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgchr_login);

        mProgressBar = findViewById(R.id.progress);

        applyWindowInsets();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.imgchr_login_title);
        toolbar.inflateMenu(R.menu.imgchr_login);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_done) {
                confirmLogin();
                return true;
            }
            return false;
        });

        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        CookieManager.getInstance().setAcceptCookie(true);

        mProgressBar.setVisibility(View.VISIBLE);
        mWebView.loadUrl(ImgchrClient.getLoginUrl());
    }

    /**
     * Android 15+ 强制应用全屏显示，需要自行处理系统栏内边距，避免标题栏被状态栏遮挡。
     */
    private void applyWindowInsets() {
        if (Build.VERSION.SDK_INT < SDK_VERSION_ANDROID_15) {
            return;
        }

        final View root = findViewById(R.id.root);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            view.setPadding(insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });
    }

    private void confirmLogin() {
        if (mChecking) {
            return;
        }
        mChecking = true;
        mProgressBar.setVisibility(View.VISIBLE);

        final String cookie = CookieManager.getInstance().getCookie(ImgchrClient.getHomeUrl());
        new Thread(() -> {
            boolean loggedIn = false;
            try {
                loggedIn = ImgchrClient.isLoggedIn(cookie);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final boolean result = loggedIn;
            runOnUiThread(() -> {
                mChecking = false;
                mProgressBar.setVisibility(View.GONE);
                if (result) {
                    ImgchrAccount.saveCookie(getApplicationContext(), cookie);
                    Toast.makeText(this, R.string.imgchr_login_success, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, R.string.imgchr_login_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }, "imgchr-login-check").start();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}

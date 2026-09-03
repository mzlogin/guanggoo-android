package org.mazhuang.guanggoo.data.task;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public abstract class BaseTask<T> implements Runnable {
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected OnResponseListener<T> mListener;

    protected boolean mIsCanceled = false;

    BaseTask(OnResponseListener<T> listener) {
        mListener = listener;
    }

    public void cancel() {
        mIsCanceled = true;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    protected void successOnUI(final T data) {
        if (!mIsCanceled && mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSucceed(data);
                }
            });
        }
    }

    protected void failedOnUI(final String msg) {
        if (!mIsCanceled && mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailed(msg);
                }
            });
        }
    }

    protected Connection getConnection(String url) {
        Connection connection = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Linux; Android 9; Mobile) AppleWebKit/537.36 "
                        + "Chrome/120.0.0.0 Mobile Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");

        Map<String, String> cookies = getCookies();
        if (cookies.size() > 0) {
            connection.cookies(cookies);
        }

        return connection;
    }

    protected Document get(String url) throws IOException {
        Document doc = getConnection(url).get();
        checkNotification(doc);
        return fixLink(doc);
    }

    protected Map<String, String> getCookies() {
        Map<String, String> cookies = new HashMap<>();
        syncWebViewCookies();
        String cookieString = PrefsUtil.getString(App.getInstance(), ConstantUtil.KEY_COOKIE, "");
        if (!TextUtils.isEmpty(cookieString)) {

            try {
                JSONObject jsonObject = new JSONObject(cookieString);

                Iterator it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = String.valueOf(it.next());
                    cookies.put(key, (String) jsonObject.get(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return cookies;
    }

    /**
     * The redesigned login runs in WebView. Its cookies survive an app restart,
     * but older builds only copied them after the login callback, leaving the
     * native side apparently logged out. Import the browser cookie jar before
     * every native request so an existing WebView session is restored too.
     */
    private void syncWebViewCookies() {
        String header = CookieManager.getInstance().getCookie(ConstantUtil.BASE_URL);
        if (TextUtils.isEmpty(header) || !header.contains("user=")) {
            return;
        }
        try {
            JSONObject json = new JSONObject();
            for (String item : header.split(";")) {
                String cookie = item.trim();
                int separator = cookie.indexOf('=');
                if (separator > 0) {
                    json.put(cookie.substring(0, separator), cookie.substring(separator + 1));
                }
            }
            if (json.has("user")) {
                PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_COOKIE, json.toString());
                if (json.has(ConstantUtil.KEY_XSRF)) {
                    PrefsUtil.putString(App.getInstance(), ConstantUtil.KEY_XSRF,
                            json.optString(ConstantUtil.KEY_XSRF));
                }
            }
        } catch (JSONException ignored) {
            // Keep the previously persisted native cookies if WebView data is malformed.
        }
    }

    protected String getCookieValue(String key) {
        Map<String, String> cookies = getCookies();
        return cookies.get(key);
    }

    protected String getXsrf() {
        return PrefsUtil.getString(App.getInstance(), ConstantUtil.KEY_XSRF, "");
    }

    protected <T extends Element> T fixLink(T element) {
        Elements links = element.select("[href]");
        for (Element link : links) {
            String href = link.attr("href");
            if (!TextUtils.isEmpty(href)) {
                if (href.toLowerCase().startsWith("http")) {
                    continue;
                }
                link.attr("href", link.absUrl("href"));
            }
        }
        links = element.select("[src]");
        for (Element link : links) {
            String src = link.attr("src");
            if (!TextUtils.isEmpty(src)) {
                if (src.toLowerCase().startsWith("http")) {
                    continue;
                }
                link.attr("src", link.absUrl("src"));
            }
        }
        return element;
    }

    protected void checkNotification(Document doc) {
        Elements elements = doc.select("a.contextually-unread");
        final boolean hasNotifications = !elements.isEmpty();
        mHandler.post(() -> App.getInstance().mGlobal.hasNotifications.setValue(hasNotifications));
    }

    protected boolean checkAuth(Document doc) {
        Elements elements = doc.select("div.usercard");
        if (!elements.isEmpty()) {
            Element usercardElement = elements.first();

            Element usernameElement = usercardElement.select("div.username").first();
            Element avatarElement = usercardElement.select("img.avatar").first();
            if (usernameElement != null) {
                AuthInfoManager.getInstance().setUsername(usernameElement.text());
            }
            if (avatarElement != null) {
                AuthInfoManager.getInstance().setAvatar(avatarElement.attr("src"));
            }
            return true;
        }
        return false;
    }

    /**
     * 尝试补偿登录状态
     * @param doc -
     */
    protected void tryFixAuthStatus(Document doc) {
        if (doc == null) {
            return;
        }

        if (TextUtils.isEmpty(AuthInfoManager.getInstance().getUsername())) {
            checkAuth(doc);
        }
    }
}

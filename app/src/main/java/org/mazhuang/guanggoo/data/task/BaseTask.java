package org.mazhuang.guanggoo.data.task;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;
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

    /**
     * 兜底的错误提示：异常没有可用信息时使用默认文案，避免出现空的提示。
     */
    protected String errorMessage(Throwable e, String fallback) {
        String message = (e == null) ? null : e.getMessage();
        return TextUtils.isEmpty(message) ? fallback : message;
    }

    /**
     * 任务内部未捕获异常的兜底处理，由调度器在发生意外异常时调用。
     */
    public void handleUnexpectedError(Throwable e) {
        // 这类异常以前会直接导致崩溃，现在虽然兜底处理了，但仍上报便于定位问题
        CrashReport.postCatchedException(e);
        failedOnUI(errorMessage(e, "操作失败，请稍后重试"));
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
        Connection connection = Jsoup.connect(url);

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
        if (doc == null) {
            return false;
        }
        Elements elements = doc.select("div.usercard");
        if (!elements.isEmpty()) {
            Element usercardElement = elements.first();
            Element usernameElement = usercardElement.select("div.username").first();
            if (usernameElement == null) {
                return false;
            }
            Element avatarElement = usercardElement.select("img.avatar").first();

            AuthInfoManager.getInstance().setUsername(usernameElement.text());
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

package org.mazhuang.guanggoo.data.task;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.PrefsUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mazhuang on 2017/9/16.
 */

public abstract class BaseTask<T> implements Runnable {
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected OnResponseListener<T> mListener;

    BaseTask(OnResponseListener<T> listener) {
        mListener = listener;
    }

    protected void successOnUI(final T data) {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSucceed(data);
                }
            });
        }
    }

    protected void failedOnUI(final String msg) {
        if (mListener != null) {
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

        String cookieString = PrefsUtil.getString(App.getInstance(), PrefsUtil.KEY_COOKIE, "");
        if (!TextUtils.isEmpty(cookieString)) {

            Map<String, String> cookies = new HashMap<>();

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

            if (cookies.size() > 0) {
                connection.cookies(cookies);
            }
        }

        return connection;
    }
}

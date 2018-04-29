package org.mazhuang.guanggoo.data.task;

import android.text.TextUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mazhuang
 * @date 2018/4/29
 */
public class FollowUserTask extends BaseTask<Boolean> {

    private String mUrl;

    public FollowUserTask(String url, OnResponseListener<Boolean> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        String xsrf = getXsrf();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> cookies = getCookies();
        if (!cookies.containsKey(ConstantUtil.KEY_XSRF)) {
            cookies.put(ConstantUtil.KEY_XSRF, xsrf);
        }

        String username = UrlUtil.getUserName(mUrl);

        if (TextUtils.isEmpty(username)) {
            failedOnUI("解析用户名出错");
            return;
        };

        try {
            Jsoup.connect(mUrl).cookies(cookies).headers(headers).method
                    (Connection.Method.GET).execute();

            String userProfileUrl = String.format(ConstantUtil.USER_PROFILE_BASE_URL, username);

            new GetUserProfileTask(userProfileUrl, new OnResponseListener<UserProfile>() {
                @Override
                public void onSucceed(UserProfile data) {
                    successOnUI(data.isFollowed());
                }

                @Override
                public void onFailed(String msg) {
                    failedOnUI(msg);
                }
            }).run();
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
        }

    }
}

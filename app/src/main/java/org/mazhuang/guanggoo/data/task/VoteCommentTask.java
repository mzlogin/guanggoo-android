package org.mazhuang.guanggoo.data.task;

import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mazhuang
 * @date 2018/4/26
 */
public class VoteCommentTask extends BaseTask<Boolean> {

    private String mUrl;

    public VoteCommentTask(String url, OnResponseListener<Boolean> listener) {
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

        try {
            Connection.Response res = Jsoup.connect(mUrl).cookies(cookies).headers(headers).method
                    (Connection.Method.GET).execute();
            if (res.statusCode() == ConstantUtil.HTTP_STATUS_200 || res.statusCode() == ConstantUtil.HTTP_STATUS_302) {
                Gson gson = new Gson();
                BaseResponse response = gson.fromJson(res.body(), BaseResponse.class);
                if (response.getSuccess() == 1) {
                    // 赞成功
                    successOnUI(true);
                } else {
                    // 以前已经赞过该评论
                    successOnUI(false);
                }
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        failedOnUI("失败");
    }
}

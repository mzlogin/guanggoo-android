package org.mazhuang.guanggoo.data.task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mazhuang on 2017/9/26.
 */

public class CommentTask extends BaseTask<String> {
    private String mUrl;
    private String mContent;

    public CommentTask(String url, String content, OnResponseListener<String> listener) {
        super(listener);
        mUrl = url;
        mContent = content;
    }

    @Override
    public void run() {

        String xsrf = getXsrf();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> datas = new HashMap<>();
        datas.put("tid", UrlUtil.getTid(mUrl));
        datas.put("content", mContent);
        datas.put(ConstantUtil.KEY_XSRF, xsrf);

        Map<String, String> cookies = getCookies();
        if (!cookies.containsKey(ConstantUtil.KEY_XSRF)) {
            cookies.put(ConstantUtil.KEY_XSRF, xsrf);
        }

        try {
            Connection.Response res = Jsoup.connect(mUrl).cookies(cookies).headers(headers).data(datas).method(Connection.Method.POST).execute();
            if (res.statusCode() == 200 || res.statusCode() == 302) {
                successOnUI("评论成功");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        failedOnUI("评论失败");
    }
}

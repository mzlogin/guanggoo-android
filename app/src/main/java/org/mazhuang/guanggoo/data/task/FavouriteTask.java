package org.mazhuang.guanggoo.data.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 *
 * @author guofeng007
 * @date 2017/9/26
 */

public class FavouriteTask extends BaseTask<String> {
    private String mUrl;

    public FavouriteTask(String url,  OnResponseListener<String> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {

        String xsrf = getXsrf();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");

        Map<String, String> datas = new HashMap<>();
        datas.put(ConstantUtil.KEY_XSRF, xsrf);

        Map<String, String> cookies = getCookies();
        if (!cookies.containsKey(ConstantUtil.KEY_XSRF)) {
            cookies.put(ConstantUtil.KEY_XSRF, xsrf);
        }

        try {
            Connection.Response res = Jsoup.connect(mUrl).cookies(cookies).headers(headers).data(datas).method
                    (Connection.Method.GET).execute();
            if (res.statusCode() == ConstantUtil.HTTP_STATUS_200 || res.statusCode() == ConstantUtil.HTTP_STATUS_302) {
                successOnUI("成功");
                return;
            } else if (res.statusCode() == ConstantUtil.HTTP_STATUS_304) {
                failedOnUI("不能收藏自己的主题");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        failedOnUI("失败");
    }
}

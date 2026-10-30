package org.mazhuang.guanggoo.data.task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;

/**
 * 跟随重定向，解析出最终地址（如搜索结果中的回复命中链接会跳转到对应主题）。
 *
 * @author mazhuang
 */
public class ResolveUrlTask extends BaseTask<String> {

    private final String mUrl;

    public ResolveUrlTask(String url, OnResponseListener<String> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        try {
            Connection.Response response = Jsoup.connect(mUrl)
                    .cookies(getCookies())
                    .method(Connection.Method.GET)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .timeout(30000)
                    .execute();
            successOnUI(response.url().toString());
        } catch (Exception e) {
            e.printStackTrace();
            failedOnUI("打开帖子失败");
        }
    }
}

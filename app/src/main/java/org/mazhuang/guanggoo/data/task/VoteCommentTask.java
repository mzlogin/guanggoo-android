package org.mazhuang.guanggoo.data.task;

import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.VoteCommentResponse;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mazhuang
 * @date 2018/4/26
 */
public class VoteCommentTask extends BaseTask<VoteCommentResponse> {

    private String mUrl;
    private String mVoteValue;

    public VoteCommentTask(String url, String voteValue, OnResponseListener<VoteCommentResponse> listener) {
        super(listener);
        mUrl = url;
        mVoteValue = voteValue;
    }

    @Override
    public void run() {

        String xsrf = getXsrf();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> datas = new HashMap<>();
        datas.put("voted", mVoteValue);
        datas.put(ConstantUtil.KEY_XSRF, xsrf);

        Map<String, String> cookies = getCookies();
        if (!cookies.containsKey(ConstantUtil.KEY_XSRF)) {
            cookies.put(ConstantUtil.KEY_XSRF, xsrf);
        }

        try {
            Connection.Response res = Jsoup.connect(mUrl).cookies(cookies).headers(headers).ignoreContentType(true).data(datas).method
                    (Connection.Method.POST).execute();
            if (res.statusCode() == ConstantUtil.HTTP_STATUS_200 || res.statusCode() == ConstantUtil.HTTP_STATUS_302) {
                Gson gson = new Gson();
                VoteCommentResponse response = gson.fromJson(res.body(), VoteCommentResponse.class);
                if (response.getSuccess() == 1) {
                    // 处理成功
                    successOnUI(response);
                } else {
                    // 处理失败
                    failedOnUI(response.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        failedOnUI("失败");
    }
}

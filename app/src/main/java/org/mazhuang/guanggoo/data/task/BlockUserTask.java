package org.mazhuang.guanggoo.data.task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public class BlockUserTask extends BaseTask<Boolean> {

    public static final int TYPE_BLOCK = 1;
    public static final int TYPE_UNBLOCK = 2;

    private UserProfile profile;
    private int type;

    public BlockUserTask(UserProfile profile, int type, OnResponseListener<Boolean> listener) {
        super(listener);
        this.profile = profile;
        this.type = type;
    }

    @Override
    public void run() {
        String xsrf = getXsrf();

        Map<String, String> cookies = getCookies();
        if (!cookies.containsKey(ConstantUtil.KEY_XSRF)) {
            cookies.put(ConstantUtil.KEY_XSRF, xsrf);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headers.put("Accept-Encoding", "gzip, deflate");

        String number = profile.getNumber().replaceAll("[^0-9]", "");

        String url = String.format(type == TYPE_BLOCK ? ConstantUtil.BLOCK_USER_BASE_URL : ConstantUtil.UNBLOCK_USER_BASE_URL, number);

        try {
            Jsoup.connect(url).headers(headers).cookies(cookies).method(Connection.Method.GET).execute();

            String userProfileUrl = String.format(ConstantUtil.USER_PROFILE_BASE_URL, profile.getUsername());

            new GetUserProfileTask(userProfileUrl, new OnResponseListener<UserProfile>() {
                @Override
                public void onSucceed(UserProfile data) {
                    successOnUI((type == TYPE_BLOCK) == data.isBlocked());
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

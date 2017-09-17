package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class AuthCheckTask extends BaseTask<String> {
    public AuthCheckTask(OnResponseListener<String> listener) {
        super(listener);
    }

    @Override
    public void run() {
        try {
            Document doc = getConnection(ConstantUtil.BASE_URL).get();
            Elements elements = doc.select("div.usercard");
            if (elements != null && !elements.isEmpty()) {
                Element usercardElement = elements.first();

                AuthInfoManager.getInstance().setUsername(usercardElement.select("div.username").first().text());
                AuthInfoManager.getInstance().setAvatar(usercardElement.select("img.avatar").first().attr("src"));
                successOnUI("succeed");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AuthInfoManager.getInstance().setUsername(null);
        AuthInfoManager.getInstance().setAvatar(null);
        failedOnUI("auth failed");
    }
}

package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public class GetBlockedUserListTask extends BaseTask<List<UserProfile>> {
    public GetBlockedUserListTask(OnResponseListener<List<UserProfile>> listener) {
        super(listener);
    }

    @Override
    public void run() {

        List<UserProfile> result = new ArrayList<>();

        Document doc;

        try {
            doc = get(ConstantUtil.BLOCKED_USER_URL);
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
            return;
        }

        Elements elements = doc.select("div.member-lists .ui-content .member");

        if (elements.isEmpty()) {
            successOnUI(result);
            return;
        }

        for (Element element : elements) {
            UserProfile profile = new UserProfile();
            profile.setAvatar(element.select("img.avatar").attr("src"));
            profile.setUsername(element.select("span.username a").text());
            profile.setUrl(element.select("span.username a").first().absUrl("href"));

            result.add(profile);
        }

        successOnUI(result);

    }
}

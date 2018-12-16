package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.ListResult;
import org.mazhuang.guanggoo.data.entity.Meta;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.Notification;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.User;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class GetNotificationListTask extends BaseTask<ListResult<Notification>> implements Runnable {

    private String mUrl;

    public GetNotificationListTask(String url, OnResponseListener<ListResult<Notification>> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        List<Notification> notifications = new ArrayList<>();

        boolean succeed = false;
        boolean hasMore = false;
        try {
            Document doc = get(mUrl);

            Elements elements = doc.select("div.notification-item");

            for (Element element : elements) {
                Notification notification = createNotificationFromElement(element);
                notifications.add(notification);
            }

            succeed = true;

            Elements paginationElements = doc.select("ul.pagination");
            if (!paginationElements.isEmpty()) {
                Elements disabledElements = paginationElements.select("li.disabled");
                if (disabledElements.isEmpty()) {
                    hasMore = true;
                } else if (disabledElements.last() != null) {
                    Elements disableLinkElements = disabledElements.last().select("a");
                    if (!ConstantUtil.NEXT_PAGE.equals(disableLinkElements.text())) {
                        hasMore = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (succeed) {
            ListResult<Notification> notificationList = new ListResult<>();
            notificationList.setData(notifications);
            notificationList.setHasMore(hasMore);
            successOnUI(notificationList);
        } else {
            failedOnUI("获取消息提醒列表失败");
        }
    }

    public static Notification createNotificationFromElement(Element element) {
        Notification notification = new Notification();

        Element userElement = element.select("a").first();
        if (userElement != null) {
            UserProfile user = new UserProfile();
            user.setUsername(UrlUtil.getUserName(userElement.absUrl("href")));
            user.setUrl(userElement.absUrl("href"));
            Element avatarElement = userElement.select("img").first();
            if (avatarElement != null) {
                user.setAvatar(avatarElement.attr("src"));
            }
            notification.setUser(user);
        }

        Element titleSpanElement = element.select("span.title").first();

        if (titleSpanElement != null) {

            if (titleSpanElement.text().endsWith("中提到了你")) {
                notification.setType(Notification.TYPE_MENTIONED);
            } else {
                notification.setType(Notification.TYPE_REPLY);
            }

            Element topicElement = titleSpanElement.select("a").last();
            if (topicElement != null) {
                Topic topic = new Topic();
                topic.setUrl(topicElement.absUrl("href"));
                topic.setTitle(topicElement.text());

                notification.setTopic(topic);
            }
        }

        Element contentElement = element.select("div.content").first();
        if (contentElement != null) {
            notification.setContent(contentElement.outerHtml());
        }

        return notification;
    }
}

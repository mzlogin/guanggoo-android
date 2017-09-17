package org.mazhuang.guanggoo.data.task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Meta;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class GetTopicTask extends BaseTask<List<Topic>> implements Runnable {

    private String mUrl;

    public GetTopicTask(String url, OnResponseListener<List<Topic>> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        List<Topic> topics = new ArrayList<>();
        Connection connection = Jsoup.connect(mUrl);

        try {
            Document doc = Jsoup.connect(mUrl).get();

            Elements elements = doc.select("div.topic-item");

            for (Element element : elements) {
                Topic topic = createTopicFromElement(element);
                topics.add(topic);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (topics.size() > 0) {
            successOnUI(topics);
        } else {
            failedOnUI("获取主题列表失败");
        }
    }

    private Topic createTopicFromElement(Element element) {
        Topic topic = new Topic();

        Element titleElement = element.select("h3.title").select("a").first();
        topic.setTitle(titleElement.text());
        topic.setUrl(titleElement.absUrl("href"));

        topic.setAvatar(element.select("img.avatar").attr("src"));

        Element metaElement = element.select("div.meta").first();
        Meta meta = createMetaFromElement(metaElement);

        topic.setMeta(meta);

        Elements countElements = element.select("div.count");
        if (countElements != null && !countElements.isEmpty()) {
            topic.setCount(Integer.valueOf(countElements.first().select("a").first().text()));
        }

        return topic;
    }

    private Meta createMetaFromElement(Element element) {
        Meta meta = new Meta();

        Element nodeElement = element.select("span.node").select("a").first();
        Node node = new Node();
        node.setTitle(nodeElement.text());
        node.setUrl(nodeElement.absUrl("href"));

        meta.setNode(node);

        Element userElement = element.select("span.username").select("a").first();
        User user = new User();
        user.setUsername(userElement.text());
        user.setUrl(userElement.absUrl("href"));

        meta.setAuthor(user);

        meta.setLastTouched(element.select("span.last-touched").text());

        Element lastReplyUserElement = element.select("span.last-reply-username").select("a").first();
        if (lastReplyUserElement != null) {
            User lastReplyUser = new User();
            lastReplyUser.setUsername(lastReplyUserElement.select("strong").text());
            lastReplyUser.setUrl(lastReplyUserElement.absUrl("href"));

            meta.setLastReplyUser(lastReplyUser);
        }

        return meta;
    }
}

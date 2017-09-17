package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.topicdetail.TopicDetailContract;

import java.io.IOException;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class GetTopicDetailTask extends BaseTask<TopicDetail> {

    private String mUrl;

    public GetTopicDetailTask(String url, OnResponseListener<TopicDetail> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        Document doc;

        try {
            doc = getConnection(mUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
            return;
        }

        Elements elements = doc.select("div.ui-header");

        if (elements == null || elements.size() == 0) {
            failedOnUI("找不到主题元信息");
            return;
        }

        TopicDetail topicDetail = new TopicDetail();

        Topic topic = GetTopicListTask.createTopicFromElement(elements.first());

        topicDetail.setTopic(topic);

        elements = doc.select("div.ui-content");

        if (elements == null || elements.size() == 0) {
            failedOnUI("找不到主题内容");
            return;
        }

        topicDetail.setContent(elements.first().outerHtml());

        successOnUI(topicDetail);
    }
}

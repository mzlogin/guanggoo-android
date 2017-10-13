package org.mazhuang.guanggoo.data.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
            doc = get(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
            return;
        }

        Elements topicDetailElements = doc.select("div.topic-detail");

        if (topicDetailElements.isEmpty()) {
            failedOnUI("找不到主题详情");
            return;
        }

        Elements elements = topicDetailElements.select("div.ui-header");

        if (elements.isEmpty()) {
            failedOnUI("找不到主题元信息");
            return;
        }

        TopicDetail topicDetail = new TopicDetail();

        Topic topic = GetTopicListTask.createTopicFromElement(elements.first());

        topicDetail.setTopic(topic);

        elements = topicDetailElements.select("div.ui-content");

        if (elements.isEmpty()) {
            failedOnUI("找不到主题内容");
            return;
        }

        topicDetail.setContent(elements.first().outerHtml());

        Elements commentsElements = doc.select("div.topic-reply");

        Map<Integer, Comment> comments = GetCommentsTask.getCommentsFromElements(commentsElements);

        topicDetail.setComments(comments);

        successOnUI(topicDetail);
    }
}

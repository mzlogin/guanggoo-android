package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.Favorite;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicDetail;

import java.io.IOException;
import java.util.Map;

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
        // 解析收藏
        Elements favouriteElement = doc.select(".J_topicFavorite");
        if(favouriteElement!=null){
            String hrefUrl = favouriteElement.attr("href");
            String dataType = favouriteElement.attr("data-type");
            String text = favouriteElement.text();
            Favorite favorite = new Favorite();
            favorite.setUrl(hrefUrl);
            favorite.setDataType(dataType);
            favorite.setText(text);
            topicDetail.setFavorite(favorite);
        }



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

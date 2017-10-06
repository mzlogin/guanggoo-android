package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Reply;
import org.mazhuang.guanggoo.data.entity.ReplyList;
import org.mazhuang.guanggoo.data.entity.Topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/10/6.
 */

public class GetReplyListTask extends BaseTask<ReplyList> {

    private String mUrl;

    public GetReplyListTask(String url, OnResponseListener<ReplyList> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        List<Reply> replies = new ArrayList<>();

        boolean succeed = false;
        boolean hasMore = false;

        try {
            Document doc = getConnection(mUrl).get();

            Elements elements = doc.select("div.reply-item");

            for (Element element : elements) {
                Reply reply = new Reply();

                Elements topicElements = element.select("span.title a");
                if (topicElements.isEmpty()) {
                    continue;
                }

                Topic topic = new Topic();
                topic.setTitle(topicElements.first().text());
                topic.setUrl(topicElements.first().absUrl("href"));

                reply.setTopic(topic);

                Elements contentElements = element.select("div.content");
                if (contentElements.isEmpty()) {
                    continue;
                }

                reply.setContent(contentElements.outerHtml());

                replies.add(reply);
            }

            succeed = true;

            Elements paginationElements = doc.select("ul.pagination");
            if (!paginationElements.isEmpty()) {
                Elements disabledElements = paginationElements.select("li.disabled");
                if (disabledElements.isEmpty()) {
                    hasMore = true;
                } else if (disabledElements.last() != null) {
                    Elements disableLinkElements = disabledElements.last().select("a");
                    if (!"下一页".equals(disableLinkElements.text())) {
                        hasMore = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (succeed) {
            ReplyList replyList = new ReplyList();
            replyList.setReplies(replies);
            replyList.setHasMore(hasMore);
            successOnUI(replyList);
        } else {
            failedOnUI("获取回复列表失败");
        }
    }
}

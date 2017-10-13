package org.mazhuang.guanggoo.data.task;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mazhuang on 2017/9/18.
 */

public class GetCommentsTask extends BaseTask<Map<Integer, Comment>> {

    private String mUrl;

    GetCommentsTask(String url, OnResponseListener<Map<Integer, Comment>> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        try {
            Document doc = get(mUrl);
            Map<Integer, Comment> comments = getCommentsFromElements(doc.select("div.topic-reply"));
            successOnUI(comments);
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
        }
    }

    public static Map<Integer, Comment> getCommentsFromElements(Elements elements) {
        TreeMap<Integer, Comment> comments = new TreeMap<>();

        Elements replyItems = elements.select("div.reply-item");

        for (Element replyItem : replyItems) {
            Comment comment = getCommentFromElement(replyItem);
            comments.put(comment.getMeta().getFloor(), comment);
        }

        return comments.descendingMap();
    }

    public static Comment getCommentFromElement(Element element) {
        Comment comment = new Comment();

        comment.setAvatar(element.select("img.avatar").attr("src"));

        Element metaElement = element.select("div.meta").first();
        Comment.Meta meta = new Comment.Meta();

        Element replyUsernameElement = metaElement.select("a.reply-username").first();
        User replier = new User();
        replier.setUrl(replyUsernameElement.absUrl("href"));
        replier.setUsername(replyUsernameElement.select("span.username").text());
        meta.setReplier(replier);

        meta.setTime(metaElement.select("span.time").text());

        try {
            meta.setFloor(Integer.valueOf(metaElement.select("span.fr.floor").first().text().replaceAll("[^\\d]", "")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            meta.setFloor(0);
        }

        Element voteElement = metaElement.select("a.J_replyVote").first();
        Comment.Vote vote = new Comment.Vote();
        vote.setUrl(voteElement.absUrl("href"));
        try {
            vote.setCount(Integer.valueOf(voteElement.attr("data-count")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            vote.setCount(0);
        }
        meta.setVote(vote);

        comment.setMeta(meta);

        comment.setContent(element.select("span.content").outerHtml());

        return comment;
    }
}

package org.mazhuang.guanggoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.task.GetCommentsTask;
import org.mazhuang.guanggoo.data.task.GetTopicListTask;

/**
 * 页面结构变化时解析不能抛异常，否则会直接导致应用崩溃。
 */
public class ParsingRobustnessTest {

    @Test
    public void parseTopicWithEmptyElement() {
        Document doc = Jsoup.parse("<div class=\"topic-item\"></div>");
        Element element = doc.selectFirst("div.topic-item");

        Topic topic = GetTopicListTask.createTopicFromElement(element);

        assertNotNull(topic);
        assertNotNull(topic.getMeta());
        assertNotNull(topic.getMeta().getNode());
        assertNotNull(topic.getMeta().getAuthor());
        assertEquals(0, topic.getCount());
    }

    @Test
    public void parseTopicWithUnexpectedStructure() {
        Document doc = Jsoup.parse("<div class=\"topic-item\">"
                + "<h3 class=\"title\"><a href=\"/t/1\">标题</a></h3>"
                + "<div class=\"meta\"><span class=\"node\"></span><span class=\"username\"></span></div>"
                + "<div class=\"count\"><span>评论</span></div>"
                + "</div>", "https://www.guozaoke.com/");
        Element element = doc.selectFirst("div.topic-item");

        Topic topic = GetTopicListTask.createTopicFromElement(element);

        assertNotNull(topic);
        assertEquals("标题", topic.getTitle());
        assertTrue(topic.getUrl().endsWith("/t/1"));
        assertEquals(0, topic.getCount());
    }

    @Test
    public void parseCommentWithEmptyElement() {
        Document doc = Jsoup.parse("<div class=\"reply-item\"></div>");
        Element element = doc.selectFirst("div.reply-item");

        Comment comment = GetCommentsTask.getCommentFromElement(element);

        assertNotNull(comment);
        assertNotNull(comment.getMeta());
        assertNotNull(comment.getMeta().getReplier());
        assertNotNull(comment.getMeta().getVote());
        assertEquals(0, comment.getMeta().getFloor());
    }
}

package org.mazhuang.guanggoo.data.entity;

import java.util.List;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class TopicDetail {
    private Topic topic;
    private String content;
    private List<Comment> comments;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

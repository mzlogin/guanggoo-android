package org.mazhuang.guanggoo.data.entity;

/**
 * Created by mazhuang on 2017/10/6.
 */

public class Reply {
    private Topic topic;
    private String content;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

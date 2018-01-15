package org.mazhuang.guanggoo.data.entity;

import java.util.List;

/**
 *
 * @author mazhuang
 * @date 2017/10/1
 */

public class TopicList {
    private List<Topic> topics;
    private boolean hasMore;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}

package org.mazhuang.guanggoo.data.entity;

import java.util.List;

/**
 * Created by mazhuang on 2017/10/6.
 */

public class ReplyList {
    private List<Reply> replies;
    private boolean hasMore;

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}

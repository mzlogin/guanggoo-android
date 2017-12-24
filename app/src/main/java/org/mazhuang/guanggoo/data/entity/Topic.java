package org.mazhuang.guanggoo.data.entity;

import android.content.Context;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class Topic {
    private String title;
    private String url;
    private String avatar;
    private Meta meta;
    private int count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLastReplyUserName() {
        if (meta != null && meta.getLastReplyUser() != null) {
            return meta.getLastReplyUser().getUsername();
        }

        return null;
    }
}

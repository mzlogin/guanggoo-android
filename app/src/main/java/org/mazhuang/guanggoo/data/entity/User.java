package org.mazhuang.guanggoo.data.entity;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class User {
    private String username;
    private String url;
    private boolean followed;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}

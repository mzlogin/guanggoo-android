package org.mazhuang.guanggoo.data.entity;

/**
 *
 * @author mazhuang
 * @date 2017/9/17
 */

public class Comment {
    private String avatar;
    private Meta meta;
    private String content;

    public static class Meta {
        private User replier;
        private String time;
        private int floor;
        private Vote vote;

        public User getReplier() {
            return replier;
        }

        public void setReplier(User replier) {
            this.replier = replier;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public Vote getVote() {
            return vote;
        }

        public void setVote(Vote vote) {
            this.vote = vote;
        }
    }

    public static class Vote {
        private String url;
        private int count;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

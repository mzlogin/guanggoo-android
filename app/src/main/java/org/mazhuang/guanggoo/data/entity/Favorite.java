package org.mazhuang.guanggoo.data.entity;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class Favorite {
    public static final String STATE_UNFAVORITE = "取消收藏";
    public static final String STATE_FAVORITE = "收藏";

    public static final String TYPE_FAVORITE = "favorite";

    private String text;
    private String url;
    private String dataType;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}

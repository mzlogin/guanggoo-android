package org.mazhuang.guanggoo.data.entity;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class Favorite {
    public static final String TYPE_NOT_FAVORITE = "favorite";

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.widget.ImageView;

import org.mazhuang.guanggoo.GlideApp;

/**
 * @author mazhuang
 * @date 2018/12/23
 */
public class GlideUtil {
    private GlideUtil() {}

    public static void loadImage(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }
}

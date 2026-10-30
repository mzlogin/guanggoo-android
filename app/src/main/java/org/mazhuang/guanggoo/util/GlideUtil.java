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
        if (imageView == null) {
            return;
        }

        try {
            GlideApp.with(imageView.getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e) {
            // 页面已销毁时 Glide 会抛异常，忽略即可
            e.printStackTrace();
        }
    }
}

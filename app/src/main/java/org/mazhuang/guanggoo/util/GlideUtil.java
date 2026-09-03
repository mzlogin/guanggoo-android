package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.widget.ImageView;

import org.mazhuang.guanggoo.GlideApp;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

/**
 * @author mazhuang
 * @date 2018/12/23
 */
public class GlideUtil {
    private GlideUtil() {}

    public static void loadImage(ImageView imageView, String url) {
        String normalizedUrl = normalizeUrl(url);
        if (normalizedUrl == null) {
            return;
        }
        GlideUrl glideUrl = new GlideUrl(normalizedUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 9; Mobile) AppleWebKit/537.36 Chrome/120.0.0.0 Mobile Safari/537.36")
                .addHeader("Referer", "https://www.guozaoke.com/")
                .build());
        GlideApp.with(imageView.getContext())
                .load(glideUrl)
                .centerCrop()
                .into(imageView);
    }

    /** The current CDN emits avatar paths with a double slash after the host. */
    private static String normalizeUrl(String url) {
        if (url == null) {
            return null;
        }
        return url.replace("://", "§").replaceAll("/{2,}", "/").replace("§", "://");
    }
}

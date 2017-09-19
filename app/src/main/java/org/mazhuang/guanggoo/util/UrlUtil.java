package org.mazhuang.guanggoo.util;

import java.util.Locale;

/**
 * Created by mazhuang on 2017/9/19.
 */

public abstract class UrlUtil {
    public static String appendPage(String baseUrl, int page) {
        return String.format(Locale.US, baseUrl.contains("?") ? "%s&p=%d" : "%s?p=%d",
                baseUrl, page);
    }
}

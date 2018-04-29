package org.mazhuang.guanggoo.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mazhuang
 * @date 2017/9/19
 */

public class UrlUtil {

    private UrlUtil() {}

    private static Pattern sTidPattern = Pattern.compile("/t/(\\d+)");
    private static Pattern sNodeCodePattern = Pattern.compile("/node/(\\w+)");
    private static Pattern sUserNamePattern = Pattern.compile("/(u|user)/(\\w+)");

    public static String appendPage(String baseUrl, int page) {
        return String.format(Locale.US, baseUrl.contains("?") ? "%s&p=%d" : "%s?p=%d",
                baseUrl, page);
    }

    public static String getTid(String url) {
        Matcher m = sTidPattern.matcher(url);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public static String getNodeCode(String url) {
        Matcher m = sNodeCodePattern.matcher(url);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public static String getUserName(String url) {
        Matcher m = sUserNamePattern.matcher(url);
        if (m.find()) {
            return m.group(2);
        } else {
            return null;
        }
    }

    public static String removeQuery(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url.split("\\?")[0];
            url = url.split("#")[0];
        }
        return url;
    }

    public static String urlEncode(String content) {
        try {
            return URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return content;
        }
    }
}

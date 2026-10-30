package org.mazhuang.guanggoo.imgchr;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.entity.UploadImageResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路过图床（imgchr.com，Chevereto）网页上传接口封装。
 *
 * @author mazhuang
 */
public class ImgchrClient {

    private static final String HOME_URL = "https://imgchr.com/";
    private static final String LOGIN_URL = "https://imgchr.com/login";
    private static final String UPLOAD_URL = "https://imgchr.com/json";

    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 13) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36";
    private static final int TIMEOUT_MS = 30000;
    private static final int UPLOAD_TIMEOUT_MS = 120000;

    private static final Pattern AUTH_TOKEN_PATTERN =
            Pattern.compile("auth_token\\s*=\\s*\"([^\"]+)\"");

    private ImgchrClient() {
    }

    public static String getHomeUrl() {
        return HOME_URL;
    }

    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    /**
     * 登录态失效。
     */
    public static class SessionExpiredException extends IOException {
        SessionExpiredException() {
            super("登录已过期，请重新登录后再上传");
        }
    }

    public static class UploadResult {
        private final String url;
        private final String cookie;

        UploadResult(String url, String cookie) {
            this.url = url;
            this.cookie = cookie;
        }

        public String getUrl() {
            return url;
        }

        /**
         * 上传后更新的 Cookie，登录状态下用于刷新本地保存的登录态。
         */
        public String getCookie() {
            return cookie;
        }
    }

    /**
     * 校验 Cookie 对应的账号是否已登录。
     */
    public static boolean isLoggedIn(String cookieString) throws IOException {
        if (TextUtils.isEmpty(cookieString)) {
            return false;
        }
        Connection.Response home = requestHome(parseCookies(cookieString));
        return isLoggedInPage(home.body());
    }

    /**
     * 上传图片。cookieString 为空时以游客身份上传（图片 14 天后失效），
     * 携带的登录态失效时抛出 {@link SessionExpiredException}。
     */
    public static UploadResult upload(String cookieString, InputStream inputStream) throws IOException {
        Map<String, String> cookies = parseCookies(cookieString);
        Connection.Response home = requestHome(cookies);

        boolean loggedIn = isLoggedInPage(home.body());
        if (!TextUtils.isEmpty(cookieString) && !loggedIn) {
            throw new SessionExpiredException();
        }

        String authToken = extractAuthToken(home.body());
        if (TextUtils.isEmpty(authToken)) {
            throw new IOException("获取上传凭证失败，请稍后重试");
        }

        Map<String, String> sessionCookies = new HashMap<>(cookies);
        sessionCookies.putAll(home.cookies());

        String filename = UUID.randomUUID() + ".jpg";
        Connection.Response response = Jsoup.connect(UPLOAD_URL)
                .userAgent(USER_AGENT)
                .header("Referer", HOME_URL)
                .header("X-Requested-With", "XMLHttpRequest")
                .cookies(sessionCookies)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .timeout(UPLOAD_TIMEOUT_MS)
                .data("action", "upload")
                .data("type", "file")
                .data("auth_token", authToken)
                .data("nsfw", "0")
                .data("source", filename, inputStream)
                .execute();

        UploadImageResponse entity = parseResponse(response.body());
        String url = entity == null ? null : entity.getDirectImageUrl();
        if (TextUtils.isEmpty(url)) {
            String message = entity == null ? null : entity.getErrorMessage();
            throw new IOException(TextUtils.isEmpty(message) ? "图片上传失败" : message);
        }
        Map<String, String> finalCookies = new HashMap<>(sessionCookies);
        finalCookies.putAll(response.cookies());

        return new UploadResult(url, joinCookies(finalCookies));
    }

    private static Connection.Response requestHome(Map<String, String> cookies) throws IOException {
        return Jsoup.connect(HOME_URL)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .cookies(cookies)
                .execute();
    }

    private static boolean isLoggedInPage(String html) {
        return html != null && html.toLowerCase(Locale.US).contains("/logout");
    }

    private static String extractAuthToken(String html) {
        if (html == null) {
            return null;
        }
        Matcher matcher = AUTH_TOKEN_PATTERN.matcher(html);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static UploadImageResponse parseResponse(String body) {
        if (TextUtils.isEmpty(body)) {
            return null;
        }
        try {
            return new Gson().fromJson(body, UploadImageResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String joinCookies(Map<String, String> cookies) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            if (builder.length() > 0) {
                builder.append("; ");
            }
            builder.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return builder.toString();
    }

    private static Map<String, String> parseCookies(String cookieString) {
        Map<String, String> cookies = new HashMap<>();
        if (TextUtils.isEmpty(cookieString)) {
            return cookies;
        }
        for (String part : cookieString.split(";")) {
            int index = part.indexOf('=');
            if (index > 0) {
                String name = part.substring(0, index).trim();
                String value = part.substring(index + 1).trim();
                if (!TextUtils.isEmpty(name)) {
                    cookies.put(name, value);
                }
            }
        }
        return cookies;
    }
}

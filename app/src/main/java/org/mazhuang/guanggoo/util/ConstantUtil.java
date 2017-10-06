package org.mazhuang.guanggoo.util;

/**
 * Created by mazhuang on 2017/9/16.
 */

public abstract class ConstantUtil {
    public static final String BASE_URL = "http://www.guanggoo.com";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String REGISTER_URL = BASE_URL + "/register";
    public static final String BEGINNER_GUIDE_URL = BASE_URL + "/t/2657";
    public static final String NODES_CLOUD_URL = BASE_URL + "/nodes";
    public static final String LATEST_URL = BASE_URL + "/?tab=latest";
    public static final String ELITE_URL = BASE_URL + "/?tab=elite";
    public static final String USER_PROFILE_BASE_URL = BASE_URL + "/u/%s";
    public static final String USER_PROFILE_SELF_FAKE_URL = BASE_URL + "/u/0";
    public static final String USER_FAVORS_BASE_URL = BASE_URL + "/u/%s/favorites";
    public static final String USER_TOPICS_BASE_URL = BASE_URL + "/u/%s/topics";

    public static final String ABOUT_URL = "INNER_PAGE_ABOUT";

    public static final int TOPICS_PER_PAGE = 36;

    public static final int COMMENTS_PER_PAGE = 106;
    public static final String KEY_COOKIE = "cookie";
    public static final String KEY_XSRF = "_xsrf";
}

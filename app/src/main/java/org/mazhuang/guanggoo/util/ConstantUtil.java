package org.mazhuang.guanggoo.util;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class ConstantUtil {

    private ConstantUtil() {}


    public static final String HOME_URL = "HOME_PAGE";
    public static final String BASE_URL = "https://www.guozaoke.com";
    public static final String FAVORITE_URL = BASE_URL + "/favorite";
    public static final String UN_FAVORITE_URL = BASE_URL + "/unfavorite";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String REGISTER_URL = BASE_URL + "/register";
    public static final String BEGINNER_GUIDE_URL = BASE_URL + "/t/2657";
    public static final String NODES_CLOUD_URL = BASE_URL + "/nodes";
    public static final String SELECT_NODE_URL = NODES_CLOUD_URL + " ";
    public static final String BLOCKED_USER_URL = BASE_URL + "/setting/blockedUser";
    public static final String LATEST_URL = BASE_URL + "/?tab=latest";
    public static final String ELITE_URL = BASE_URL + "/?tab=elite";
    public static final String FOLLOWS_URL = BASE_URL + "/?tab=follows";
    public static final String USER_PROFILE_BASE_URL = BASE_URL + "/u/%s";
    public static final String USER_PROFILE_SELF_FAKE_URL = BASE_URL + "/u/0";
    public static final String USER_FAVORS_BASE_URL = BASE_URL + "/u/%s/favorites";
    public static final String USER_TOPICS_BASE_URL = BASE_URL + "/u/%s/topics";
    public static final String USER_REPLIES_BASE_URL = BASE_URL + "/u/%s/replies";
    public static final String FOLLOW_USER_BASE_URL = BASE_URL + "/f/user/%s";
    public static final String BLOCK_USER_BASE_URL = BASE_URL + "/u/%s/block";
    public static final String UNBLOCK_USER_BASE_URL = BASE_URL + "/u/%s/unblock";
    public static final String NEW_TOPIC_BASE_URL = BASE_URL + "/t/create/%s";
    public static final String NOTIFICATIONS_URL = BASE_URL + "/notifications";

    public static final String VERIFY_TELEPHONE_URL = BASE_URL + "/setting/phoneNum";

    public static final String ABOUT_URL = "INNER_PAGE_ABOUT";
    public static final String SEARCH_URL = "SEARCH_URL";
    public static final String SETTINGS_URL = "SETTINGS_URL";
    public static final String SETTINGS_EDIT_URL = "SETTINGS_EDIT_URL";
    public static final String FEEDBACK_URL = "FEEDBACK_URL";

    public static final int TOPICS_PER_PAGE = 36;

    public static final int FAVORITE_PER_PAGE = 16;

    public static final int COMMENTS_PER_PAGE = 106;

    public static final int REPLIES_PER_PAGE = 16;

    public static final int NOTIFICATIONS_PER_PAGE = 16;

    public static final String KEY_COOKIE = "cookie";
    public static final String KEY_XSRF = "_xsrf";
    public static final String KEY_COMMENTS_ORDER_DESC = "comments_order_desc";
    public static final String KEY_PRIVACY_POLICY_AGREED_VERSION = "privacy_policy_agreed_version";
    public static final String KEY_IMG_BED_API_KEY = "img_bed_api_key";

    public static final String NEXT_PAGE = "下一页";

    public static final int HTTP_STATUS_200 = 200;
    public static final int HTTP_STATUS_302 = 302;
    public static final int HTTP_STATUS_304 = 304;

    public static final int MAX_TOPICS = 1024;

    public static final String DOWNLOAD_URL_COOLAPK = "https://www.coolapk.com/apk/164523";
    public static final String PRIVACY_URL = "https://mazhuang.org/guanggoo-android/policy";
    public static final String IMAGE_BED_HELP_URL = "https://mazhuang.org/guanggoo-android/image-bed-help";
}

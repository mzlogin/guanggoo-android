package org.mazhuang.guanggoo.router;

import org.mazhuang.guanggoo.about.AboutFragment;
import org.mazhuang.guanggoo.about.FeedbackFragment;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.home.HomeFragment;
import org.mazhuang.guanggoo.login.LoginFragment;
import org.mazhuang.guanggoo.newtopic.NewTopicFragment;
import org.mazhuang.guanggoo.newtopic.SelectNodeFragment;
import org.mazhuang.guanggoo.nodescloud.NodesCloudFragment;
import org.mazhuang.guanggoo.notifications.NotificationsFragment;
import org.mazhuang.guanggoo.search.SearchFragment;
import org.mazhuang.guanggoo.settings.SettingsFragment;
import org.mazhuang.guanggoo.topicdetail.TopicDetailFragment;
import org.mazhuang.guanggoo.topicdetail.viewimage.ViewImageFragment;
import org.mazhuang.guanggoo.topiclist.TopicListFragment;
import org.mazhuang.guanggoo.userprofile.UserProfileFragment;
import org.mazhuang.guanggoo.userprofile.block.BlockedUserListFragment;
import org.mazhuang.guanggoo.userprofile.replies.ReplyListFragment;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.util.regex.Pattern;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public class FragmentFactory {

    private FragmentFactory() {}

    public enum PageType {
        // 无
        NONE,
        // 首页
        HOME,
        // 首页主题列表
        HOME_TOPIC_LIST,
        // 主题详情
        TOPIC_DETAIL,
        // 节点列表
        NODES_CLOUD,
        // 发表新主题时选择节点
        SELECT_NODE,
        // 节点主题列表
        NODE_TOPIC_LIST,
        // 登录
        LOGIN,
        // 个人资料页
        USER_PROFILE,
        // 个人收藏页
        USER_FAVORS,
        // 个人主题列表
        USER_TOPICS,
        // 个人回复列表
        USER_REPLIES,
        // 关于
        ABOUT,
        // 发表新主题
        NEW_TOPIC,
        // 查看图片
        VIEW_IMAGE,
        // 查看消息提醒
        VIEW_NOTIFICATIONS,
        // 搜索页面
        SEARCH,
        // 设置页面
        SETTINGS,
        // 建议与反馈
        FEEDBACK,
        // 已屏蔽用户列表
        BLOCKED_USER_LIST
    }

    private static final Pattern HOME_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com[/]?$");
    private static final Pattern TOPIC_DETAIL_PATTERN = Pattern.compile("^http://www.guanggoo.com/t/\\d+$");
    private static final Pattern NODES_CLOUD_PATTERN = Pattern.compile("^http://www.guanggoo.com/nodes$");
    private static final Pattern SELECT_NODE_PATTERN = Pattern.compile("^http://www.guanggoo.com/nodes $");
    private static final Pattern NODE_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com/node/[^/]+$");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^http://www.guanggoo.com/login$");
    private static final Pattern USER_PROFILE_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+$");
    private static final Pattern USER_FAVORS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/favorites$");
    private static final Pattern USER_TOPICS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/topics$");
    private static final Pattern USER_REPLIES_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/replies$");
    private static final Pattern NEW_TOPIC_PATTERN = Pattern.compile("^http://www.guanggoo.com/t/create/\\w+$");
    private static final Pattern VIEW_IMAGE_PATTERN = Pattern.compile("^http[s]?://.+\\.(png|jpg|jpeg)$");


    public static BaseFragment getFragmentByUrl(String url) {
        url = UrlUtil.removeQuery(url);
        return getFragmentByPageType(getPageTypeByUrl(url));
    }

    private static BaseFragment getFragmentByPageType(PageType type) {

        BaseFragment fragment;
        switch (type) {

            case HOME:
                fragment = new HomeFragment();
                break;

            case HOME_TOPIC_LIST:
            case USER_TOPICS:
            case NODE_TOPIC_LIST:
                fragment = new TopicListFragment();
                break;

            case USER_FAVORS:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new TopicListFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case TOPIC_DETAIL:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new TopicDetailFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case NODES_CLOUD:
                fragment = new NodesCloudFragment();
                break;

            case SELECT_NODE:
                fragment = new SelectNodeFragment();
                break;

            case LOGIN:
                fragment = new LoginFragment();
                break;

            case USER_PROFILE:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new UserProfileFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case USER_REPLIES:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new ReplyListFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case NEW_TOPIC:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new NewTopicFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case VIEW_IMAGE:
                fragment = new ViewImageFragment();
                break;

            case ABOUT:
                fragment = new AboutFragment();
                break;

            case VIEW_NOTIFICATIONS:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new NotificationsFragment();
                } else {
                    fragment = new LoginFragment();
                }
                break;

            case SEARCH:
                fragment = new SearchFragment();
                break;

            case SETTINGS:
                fragment = new SettingsFragment();
                break;

            case FEEDBACK:
                fragment = new FeedbackFragment();
                break;

            case BLOCKED_USER_LIST:
                fragment = new BlockedUserListFragment();
                break;

            default:
                fragment = null;
                break;
        }

        if (fragment != null) {
            fragment.setPageType(type);
        }

        return fragment;
    }

    public static PageType getPageTypeByUrl(String url) {
        if (HOME_TOPIC_LIST_PATTERN.matcher(url).find()) {
            return PageType.HOME_TOPIC_LIST;
        }

        if (TOPIC_DETAIL_PATTERN.matcher(url).find()) {
            return PageType.TOPIC_DETAIL;
        }

        if (NODES_CLOUD_PATTERN.matcher(url).find()) {
            return PageType.NODES_CLOUD;
        }

        if (SELECT_NODE_PATTERN.matcher(url).find()) {
            return PageType.SELECT_NODE;
        }

        if (NODE_TOPIC_LIST_PATTERN.matcher(url).find()) {
            return PageType.NODE_TOPIC_LIST;
        }

        if (LOGIN_PATTERN.matcher(url).find()) {
            return PageType.LOGIN;
        }

        if (USER_PROFILE_PATTERN.matcher(url).find()) {
            return PageType.USER_PROFILE;
        }

        if (USER_FAVORS_PATTERN.matcher(url).find()) {
            return PageType.USER_FAVORS;
        }

        if (USER_TOPICS_PATTERN.matcher(url).find()) {
            return PageType.USER_TOPICS;
        }

        if (USER_REPLIES_PATTERN.matcher(url).find()) {
            return PageType.USER_REPLIES;
        }

        if (NEW_TOPIC_PATTERN.matcher(url).find()) {
            return PageType.NEW_TOPIC;
        }

        if (VIEW_IMAGE_PATTERN.matcher(url).find()) {
            return PageType.VIEW_IMAGE;
        }

        if (ConstantUtil.HOME_URL.equals(url)) {
            return PageType.HOME;
        }

        if (ConstantUtil.NOTIFICATIONS_URL.equals(url)) {
            return PageType.VIEW_NOTIFICATIONS;
        }

        if (ConstantUtil.ABOUT_URL.equals(url)) {
            return PageType.ABOUT;
        }

        if (ConstantUtil.SEARCH_URL.equals(url)) {
            return PageType.SEARCH;
        }

        if (ConstantUtil.SETTINGS_URL.equals(url)) {
            return PageType.SETTINGS;
        }

        if (ConstantUtil.FEEDBACK_URL.equals(url)) {
            return PageType.FEEDBACK;
        }

        if (ConstantUtil.BLOCKED_USER_URL.equals(url)) {
            return PageType.BLOCKED_USER_LIST;
        }

        return PageType.NONE;
    }
}

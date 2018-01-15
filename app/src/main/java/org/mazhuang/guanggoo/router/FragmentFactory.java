package org.mazhuang.guanggoo.router;

import android.app.Fragment;

import org.mazhuang.guanggoo.about.AboutFragment;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.home.HomeFragment;
import org.mazhuang.guanggoo.login.LoginFragment;
import org.mazhuang.guanggoo.login.LoginPresenter;
import org.mazhuang.guanggoo.newtopic.NewTopicFragment;
import org.mazhuang.guanggoo.newtopic.NewTopicPresenter;
import org.mazhuang.guanggoo.newtopic.SelectNodeFragment;
import org.mazhuang.guanggoo.nodescloud.NodesCloudFragment;
import org.mazhuang.guanggoo.nodescloud.NodesCloudPresenter;
import org.mazhuang.guanggoo.topicdetail.TopicDetailFragment;
import org.mazhuang.guanggoo.topicdetail.TopicDetailPresenter;
import org.mazhuang.guanggoo.topicdetail.viewimage.ViewImageFragment;
import org.mazhuang.guanggoo.topicdetail.viewimage.ViewImagePresenter;
import org.mazhuang.guanggoo.topiclist.TopicListFragment;
import org.mazhuang.guanggoo.topiclist.TopicListPresenter;
import org.mazhuang.guanggoo.userprofile.UserProfileFragment;
import org.mazhuang.guanggoo.userprofile.UserProfilePresenter;
import org.mazhuang.guanggoo.userprofile.replies.ReplyListFragment;
import org.mazhuang.guanggoo.userprofile.replies.ReplyListPresenter;
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
    }

    public static final Pattern HOME_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com[/]?$");
    public static final Pattern TOPIC_DETAIL_PATTERN = Pattern.compile("^http://www.guanggoo.com/t/\\d+$");
    public static final Pattern NODES_CLOUD_PATTERN = Pattern.compile("^http://www.guanggoo.com/nodes$");
    public static final Pattern SELECT_NODE_PATTERN = Pattern.compile("^http://www.guanggoo.com/nodes $");
    public static final Pattern NODE_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com/node/[^/]+$");
    public static final Pattern LOGIN_PATTERN = Pattern.compile("^http://www.guanggoo.com/login$");
    public static final Pattern USER_PROFILE_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+$");
    public static final Pattern USER_FAVORS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/favorites$");
    public static final Pattern USER_TOPICS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/topics$");
    public static final Pattern USER_REPLIES_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/replies$");
    public static final Pattern NEW_TOPIC_PATTERN = Pattern.compile("^http://www.guanggoo.com/t/create/\\w+$");
    public static final Pattern VIEW_IMAGE_PATTERN = Pattern.compile("^http[s]?://.+\\.(png|jpg|jpeg)$");


    public static BaseFragment getFragmentByUrl(String url) {
        url = UrlUtil.removeQuery(url);
        return getFragmentByPageType(getPageTypeByUrl(url));
    }

    public static BaseFragment getFragmentByPageType(PageType type) {

        BaseFragment fragment;
        switch (type) {

            case HOME:
                fragment = new HomeFragment();
                break;

            case HOME_TOPIC_LIST:
                fragment = new TopicListFragment();
                new TopicListPresenter((TopicListFragment)fragment);
                break;

            case NODE_TOPIC_LIST:
                fragment = new TopicListFragment();
                new TopicListPresenter((TopicListFragment)fragment);
                break;

            case USER_FAVORS:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new TopicListFragment();
                    new TopicListPresenter((TopicListFragment) fragment);
                } else {
                    fragment = new LoginFragment();
                    new LoginPresenter((LoginFragment)fragment);
                }
                break;

            case USER_TOPICS:
                fragment = new TopicListFragment();
                new TopicListPresenter((TopicListFragment) fragment);
                break;

            case TOPIC_DETAIL:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new TopicDetailFragment();
                    new TopicDetailPresenter((TopicDetailFragment)fragment);
                } else {
                    fragment = new LoginFragment();
                    new LoginPresenter((LoginFragment)fragment);
                }
                break;

            case NODES_CLOUD:
                fragment = new NodesCloudFragment();
                new NodesCloudPresenter((NodesCloudFragment)fragment);
                break;

            case SELECT_NODE:
                fragment = new SelectNodeFragment();
                new NodesCloudPresenter((SelectNodeFragment)fragment);
                break;

            case LOGIN:
                fragment = new LoginFragment();
                new LoginPresenter((LoginFragment)fragment);
                break;

            case USER_PROFILE:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new UserProfileFragment();
                    new UserProfilePresenter((UserProfileFragment) fragment);
                } else {
                    fragment = new LoginFragment();
                    new LoginPresenter((LoginFragment)fragment);
                }
                break;

            case USER_REPLIES:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new ReplyListFragment();
                    new ReplyListPresenter((ReplyListFragment) fragment);
                } else {
                    fragment = new LoginFragment();
                    new LoginPresenter((LoginFragment) fragment);
                }
                break;

            case NEW_TOPIC:
                if (AuthInfoManager.getInstance().isLoginIn()) {
                    fragment = new NewTopicFragment();
                    new NewTopicPresenter((NewTopicFragment) fragment);
                } else {
                    fragment = new LoginFragment();
                    new LoginPresenter((LoginFragment) fragment);
                }
                break;

            case VIEW_IMAGE:
                fragment = new ViewImageFragment();
                new ViewImagePresenter((ViewImageFragment) fragment);
                break;

            case ABOUT:
                fragment = new AboutFragment();
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

        if (ConstantUtil.ABOUT_URL.equals(url)) {
            return PageType.ABOUT;
        }

        return PageType.NONE;
    }
}

package org.mazhuang.guanggoo;

import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.login.LoginFragment;
import org.mazhuang.guanggoo.login.LoginPresenter;
import org.mazhuang.guanggoo.nodescloud.NodesCloudFragment;
import org.mazhuang.guanggoo.nodescloud.NodesCloudPresenter;
import org.mazhuang.guanggoo.topicdetail.TopicDetailFragment;
import org.mazhuang.guanggoo.topicdetail.TopicDetailPresenter;
import org.mazhuang.guanggoo.topiclist.TopicListFragment;
import org.mazhuang.guanggoo.topiclist.TopicListPresenter;
import org.mazhuang.guanggoo.userprofile.UserProfileFragment;
import org.mazhuang.guanggoo.userprofile.UserProfilePresenter;
import org.mazhuang.guanggoo.util.UrlUtil;

import java.util.regex.Pattern;

/**
 * Created by Lenovo on 2017/9/28.
 */

public abstract class FragmentFactory {

    public enum PageType {
        NONE,
        HOME_TOPIC_LIST, // 首页主题列表
        TOPIC_DETAIL, // 主题详情
        NODES_CLOUD, // 节点列表
        NODE_TOPIC_LIST, // 节点主题列表
        LOGIN, // 登录
        USER_PROFILE, // 个人资料页
        USER_FAVORS, // 个人收藏页
        USER_TOPICS, // 个人主题列表
    }

    public static final Pattern HOME_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com[/]?$");
    public static final Pattern TOPIC_DETAIL_PATTERN = Pattern.compile("^http://www.guanggoo.com/t/\\d+$");
    public static final Pattern NODES_CLOUD_PATTERN = Pattern.compile("^http://www.guanggoo.com/nodes$");
    public static final Pattern NODE_TOPIC_LIST_PATTERN = Pattern.compile("^http://www.guanggoo.com/node/[^/]+$");
    public static final Pattern LOGIN_PATTERN = Pattern.compile("^http://www.guanggoo.com/login$");
    public static final Pattern USER_PROFILE_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+$");
    public static final Pattern USER_FAVORS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/favorites$");
    public static final Pattern USER_TOPICS_PATTERN = Pattern.compile("^http://www.guanggoo.com/u/\\w+/topics$");


    public static BaseFragment getFragmentByUrl(String url) {
        url = UrlUtil.removeQuery(url);
        return getFragmentByPageType(getPageTypeByUrl(url));
    }

    public static BaseFragment getFragmentByPageType(PageType type) {

        BaseFragment fragment;
        switch (type) {
            case HOME_TOPIC_LIST:
                fragment = new TopicListFragment();
                new TopicListPresenter((TopicListFragment)fragment);
                fragment.setClearTop(true);
                fragment.setIsHome(true);
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

            default:
                fragment = null;
                break;
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

        return PageType.NONE;
    }
}

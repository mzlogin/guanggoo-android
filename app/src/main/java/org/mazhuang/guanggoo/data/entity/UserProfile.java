package org.mazhuang.guanggoo.data.entity;

import android.text.TextUtils;

import lombok.Data;

/**
 *
 * @author Lenovo
 * @date 2017/9/29
 */
@Data
public class UserProfile {
    /**
     * 用户名（ID）
     */
    private String username;

    /**
     * 个人资料页 URL
     */
    private String url;

    /**
     * 头像图片地址
     */
    private String avatar;

    /**
     * 社区第多少号用户
     */
    private String number;

    /**
     * 注册时间
     */
    private String since;

    /**
     * 是否已经关注
     */
    private boolean followed;

    /**
     * 是否已经屏蔽
     */
    private boolean blocked;

    public boolean isValid() {
        return !TextUtils.isEmpty(avatar) &&
                !TextUtils.isEmpty(url) &&
                !TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(since) &&
                !TextUtils.isEmpty(number);
    }
}

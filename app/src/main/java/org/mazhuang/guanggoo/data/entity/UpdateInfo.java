package org.mazhuang.guanggoo.data.entity;

/**
 * 升级检测接口返回的信息。
 *
 * @author mazhuang
 */
public class UpdateInfo {
    /**
     * 最新版本的 versionCode，用于与当前安装包比较。
     */
    public int versionCode;
    /**
     * 最新版本的 versionName，仅用于展示。
     */
    public String versionName;
    /**
     * 最新版本安装包的下载地址。
     */
    public String downloadUrl;
    /**
     * 更新说明。
     */
    public String changeLog;
    /**
     * 安装包 MD5，可选，提供时会校验下载结果。
     */
    public String md5;
    /**
     * 是否强制更新，可选，默认 false。
     * 为 true 时弹框不可取消，下载失败后需重试或退出应用。
     */
    public boolean forceUpdate;
}

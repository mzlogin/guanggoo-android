package org.mazhuang.guanggoo.data.entity;

import lombok.Data;

/**
 * 帖子搜索结果。
 *
 * @author mazhuang
 */
@Data
public class SearchResult {
    /**
     * 帖子标题。
     */
    private String title;
    /**
     * 帖子链接，回复命中时为回复链接。
     */
    private String url;
    /**
     * 节点名称。
     */
    private String nodeTitle;
    /**
     * 作者用户名。
     */
    private String author;
    /**
     * 发帖时间。
     */
    private String date;
    /**
     * 命中位置，如"主题正文""回复命中"。
     */
    private String hitType;
    /**
     * 命中摘要，含 mark 高亮标签。
     */
    private String excerptHtml;
}

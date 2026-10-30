package org.mazhuang.guanggoo.data.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 帖子搜索的一页结果，nextPageUrl 为空表示没有下一页了。
 *
 * @author mazhuang
 */
@Data
public class SearchResultPage {
    private List<SearchResult> data = new ArrayList<>();
    private String nextPageUrl;
}

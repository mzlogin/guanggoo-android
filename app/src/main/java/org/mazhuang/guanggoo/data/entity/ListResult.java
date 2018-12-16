package org.mazhuang.guanggoo.data.entity;

import java.util.List;

import lombok.Data;

/**
 * 从网络获取元素列表时的统一对象，携带数据和是否有更多数据信息
 *
 * @author mazhuang
 * @date 2018/10/14
 */
@Data
public class ListResult<T> {
    private List<T> data;
    private boolean hasMore;
}

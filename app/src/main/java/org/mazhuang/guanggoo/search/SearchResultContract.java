package org.mazhuang.guanggoo.search;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.SearchResultPage;

/**
 * @author mazhuang
 */
public interface SearchResultContract {

    interface Presenter extends BasePresenter {
        /**
         * 搜索帖子（从第一页开始）
         * @param url 搜索结果页地址
         */
        void search(String url);

        /**
         * 加载下一页结果
         * @param url 下一页结果页地址
         */
        void loadMore(String url);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 搜索成功
         * @param page 一页搜索结果
         * @param append 是否为加载更多（追加到列表尾部）
         */
        void onSearchSucceed(SearchResultPage page, boolean append);

        /**
         * 搜索失败
         * @param msg 失败提示信息
         * @param append 是否为加载更多失败
         */
        void onSearchFailed(String msg, boolean append);
    }
}

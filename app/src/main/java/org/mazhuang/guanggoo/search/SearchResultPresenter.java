package org.mazhuang.guanggoo.search;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.SearchResultPage;
import org.mazhuang.guanggoo.data.task.BaseTask;
import org.mazhuang.guanggoo.data.task.GetSearchResultTask;

/**
 * @author mazhuang
 */
public class SearchResultPresenter implements SearchResultContract.Presenter {

    private final SearchResultContract.View mView;

    private BaseTask mCurrentTask;

    public SearchResultPresenter(SearchResultContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void search(String url) {
        request(url, false);
    }

    @Override
    public void loadMore(String url) {
        request(url, true);
    }

    private void request(String url, boolean append) {
        if (mCurrentTask != null) {
            mCurrentTask.cancel();
        }

        mCurrentTask = new GetSearchResultTask(url, new OnResponseListener<SearchResultPage>() {
            @Override
            public void onSucceed(SearchResultPage data) {
                mCurrentTask = null;
                mView.onSearchSucceed(data, append);
            }

            @Override
            public void onFailed(String msg) {
                mCurrentTask = null;
                mView.onSearchFailed(msg, append);
            }
        });

        NetworkTaskScheduler.getInstance().execute(mCurrentTask);
    }
}

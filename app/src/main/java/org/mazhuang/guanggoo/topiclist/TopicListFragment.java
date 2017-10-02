package org.mazhuang.guanggoo.topiclist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.entity.TopicList;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.util.List;

public class TopicListFragment extends BaseFragment<TopicListContract.Presenter> implements TopicListContract.View {

    private TopicListAdapter mAdapter;
    private boolean mLoadable = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        initParams();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, 1);
                }
            });
            if (mAdapter == null) {
                mAdapter = new TopicListAdapter(mListener);
            }
            recyclerView.setAdapter(mAdapter);

            // ref https://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(dy > 0) { //check for scroll down
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        if (mLoadable) {
                            if ( (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                mLoadable = false;
                                if (totalItemCount >= ConstantUtil.TOPICS_PER_PAGE && totalItemCount <= 1024) {
                                    mPresenter.getMoreTopic(totalItemCount / ConstantUtil.TOPICS_PER_PAGE + 1);
                                } else {
                                    Toast.makeText(getActivity(), "1024", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            });
        }

        if (!mAdapter.isFilled()) {
            mPresenter.getTopicList();
        }

        return view;
    }

    @Override
    public void onGetTopicListSucceed(TopicList topicList) {
        if (getContext() == null) {
            return;
        }

        mLoadable = topicList.isHasMore();

        mAdapter.setData(topicList.getTopics());
    }

    @Override
    public void onGetTopicListFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return getString(R.string.topic_list);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onGetMoreTopicSucceed(TopicList topicList) {
        if (getContext() == null) {
            return;
        }

        mLoadable = topicList.isHasMore();

        mAdapter.addData(topicList.getTopics());
    }

    @Override
    public void onGetMoreTopicFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

package org.mazhuang.guanggoo.topiclist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TopicListFragment extends BaseFragment<TopicListContract.Presenter> implements TopicListContract.View {

    private OnListFragmentInteractionListener mListener;
    private TopicListAdapter mAdapter;
    private boolean mLoadable = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopicListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

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
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if (mLoadable) {
                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                mLoadable = false;
                                if (totalItemCount <= 1024) {
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

        if (!mAdapter.isFiiled()) {
            mPresenter.getTopicList();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGetTopicListSucceed(List<Topic> topicList) {
        mAdapter.setData(topicList);
    }

    @Override
    public void onGetTopicListFailed(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Topic item);
    }

    @Override
    public String getTitle() {
        return "主题列表";
    }

    @Override
    public void onGetMoreTopicSucceed(List<Topic> topicList) {
        mAdapter.addData(topicList);
        mLoadable = true;
    }

    @Override
    public void onGetMoreTopicFailed(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

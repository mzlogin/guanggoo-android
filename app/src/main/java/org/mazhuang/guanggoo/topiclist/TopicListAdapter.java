package org.mazhuang.guanggoo.topiclist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.Topic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private List<Topic> mData;
    private final FragmentCallBack mListener;

    public TopicListAdapter(FragmentCallBack listener) {
        mListener = listener;
    }

    public void setData(List<Topic> data) {
        if (data == mData) {
            return;
        }

        mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<Topic> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mData.get(position);
        holder.mTitleTextView.setText(mData.get(position).getTitle());
        Glide.with(holder.mAvatarImageView.getContext())
                .load(holder.mItem.getAvatar())
                .centerCrop()
                .crossFade()
                .into(holder.mAvatarImageView);
        holder.mLastTouchedTextView.setText(holder.mItem.getMeta().getLastTouched());
        holder.mAuthorTextView.setText(holder.mItem.getMeta().getAuthor().getUsername());
        holder.mNodeTextView.setText(holder.mItem.getMeta().getNode().getTitle());
        holder.mCountTextView.setText(String.valueOf(holder.mItem.getCount()));
        holder.mLastReplyUserTextView.setText(holder.mItem.getLastReplyUserName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.openPage(holder.mItem.getUrl(), holder.mItem.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public boolean isFilled() {
        return (mData != null && mData.size() > 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.title) TextView mTitleTextView;
        @BindView(R.id.avatar) ImageView mAvatarImageView;
        @BindView(R.id.last_touched) TextView mLastTouchedTextView;
        @BindView(R.id.author) TextView mAuthorTextView;
        @BindView(R.id.node) TextView mNodeTextView;
        @BindView(R.id.last_reply_user) TextView mLastReplyUserTextView;
        @BindView(R.id.count) TextView mCountTextView;
        public Topic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.avatar, R.id.author, R.id.node, R.id.last_reply_user})
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }

            switch (v.getId()) {
                case R.id.avatar:
                case R.id.author:
                    mListener.openPage(mItem.getMeta().getAuthor().getUrl(), null);
                    break;

                case R.id.node:
                    Node node = mItem.getMeta().getNode();
                    mListener.openPage(node.getUrl(), node.getTitle());
                    break;

                case R.id.last_reply_user:
                    mListener.openPage(mItem.getMeta().getLastReplyUser().getUrl(), null);
            }
        }
    }
}

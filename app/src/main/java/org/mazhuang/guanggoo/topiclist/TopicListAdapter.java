package org.mazhuang.guanggoo.topiclist;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mazhuang
 */
public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private List<Topic> mData;
    private final FragmentCallBack mListener;

    TopicListAdapter(FragmentCallBack listener) {
        mListener = listener;
    }

    public void setData(List<Topic> data) {
        if (data == mData) {
            return;
        }

        mData = data;
        notifyDataSetChanged();
    }

    void addData(List<Topic> data) {
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
        GlideUtil.loadImage(holder.mAvatarImageView, holder.mItem.getAvatar());
        holder.mAuthorTextView.setText(holder.mItem.getMeta().getAuthor().getUsername());
        holder.mNodeTextView.setText(holder.mItem.getMeta().getNode().getTitle());
        holder.mCommentsInfoTextView.setText(getTopicCommentsInfo(holder.mCommentsInfoTextView.getContext(), holder.mItem));

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

    boolean isFilled() {
        return (mData != null && mData.size() > 0);
    }

    private String getTopicCommentsInfo(Context context, Topic topic) {
        String lastTouched = topic.getMeta().getLastTouched();
        String count = String.valueOf(topic.getCount());
        String lastReplyUser = topic.getLastReplyUserName();

        if (TextUtils.isEmpty(lastReplyUser)) {
            return context.getString(R.string.comments_info_short, count, lastTouched);
        } else {
            return context.getString(R.string.comments_info_long, count, lastReplyUser, lastTouched);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.title) TextView mTitleTextView;
        @BindView(R.id.avatar) ImageView mAvatarImageView;
        @BindView(R.id.author) TextView mAuthorTextView;
        @BindView(R.id.node) TextView mNodeTextView;
        @BindView(R.id.comments_info) TextView mCommentsInfoTextView;
        public Topic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.avatar, R.id.author, R.id.node})
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

                default:
                    break;
            }
        }
    }
}

package org.mazhuang.guanggoo.topicdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private Map<Integer, Comment> mData;
    private final FragmentCallBack mListener;

    public CommentsListAdapter(FragmentCallBack listener) {
        mListener = listener;
    }

    public void setData(Map<Integer, Comment> data) {
        if (data == mData) {
            return;
        }

        mData = data;
        notifyDataSetChanged();
    }

    public void addData(Map<Integer, Comment> data) {
        mData.putAll(data);
        notifyDataSetChanged();
    }

    public int getSmallestFloor() {
        int ret = 0;
        if (mData != null && mData.size() != 0) {
            int count = 0;
            for (Integer i : mData.keySet()) {
                count++;
                if (count == mData.keySet().size()) {
                    ret = i;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_list, parent, false);
        return new ViewHolder(view);
    }

    private Comment getItem(int position) {
        Comment comment = null;
        for (Integer i : mData.keySet()) {
            if (position == 0) {
                comment = mData.get(i);
                break;
            }
            position--;
        }

        return comment;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mLastTouchedTextView.setText(holder.mItem.getMeta().getTime());
        Glide.with(holder.mAvatarImageView.getContext())
                .load(holder.mItem.getAvatar())
                .centerCrop()
                .crossFade()
                .into(holder.mAvatarImageView);
        holder.mAuthorTextView.setText(holder.mItem.getMeta().getReplier().getUsername());
        holder.mFloorTextView.setText("#" + holder.mItem.getMeta().getFloor());
        holder.mContentTextView.setHtml(holder.mItem.getContent(), new HtmlHttpImageGetter(holder.mContentTextView));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // nothing to do now
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.avatar) ImageView mAvatarImageView;
        @BindView(R.id.last_touched) TextView mLastTouchedTextView;
        @BindView(R.id.author) TextView mAuthorTextView;
        @BindView(R.id.content) HtmlTextView mContentTextView;
        @BindView(R.id.floor) TextView mFloorTextView;
        public Comment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}

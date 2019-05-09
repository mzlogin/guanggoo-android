package org.mazhuang.guanggoo.topicdetail;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.router.FragmentFactory;
import org.mazhuang.guanggoo.util.GlideUtil;
import org.mazhuang.guanggoo.util.MyHtmlHttpImageGetter;
import org.mazhuang.guanggoo.util.UrlUtil;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * @author mazhuang
 */
public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private Map<Integer, Comment> mData;
    private final CommentsActionListener mListener;
    private BetterLinkMovementMethod.OnLinkClickListener mOnLinkClickListener = new BetterLinkMovementMethod.OnLinkClickListener() {
        @Override
        public boolean onClick(TextView textView, String url) {
            if (mListener == null || TextUtils.isEmpty(url)) {
                return false;
            }

            url = UrlUtil.removeQuery(url);

            if (FragmentFactory.PageType.NONE != FragmentFactory.getPageTypeByUrl(url)) {
                mListener.openPage(url, null);
                return true;
            }

            return false;
        }
    };

    public CommentsListAdapter(CommentsActionListener listener) {
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
        GlideUtil.loadImage(holder.mAvatarImageView, holder.mItem.getAvatar());
        holder.mAuthorTextView.setText(holder.mItem.getMeta().getReplier().getUsername());
        holder.mFloorTextView.setText("#" + holder.mItem.getMeta().getFloor());
        MyHtmlHttpImageGetter imageGetter = new MyHtmlHttpImageGetter(holder.mContentTextView);
        imageGetter.enableCompressImage(true, 30);

        holder.mContentTextView.setHtml(holder.mItem.getContent(), imageGetter);
        BetterLinkMovementMethod
                .linkifyHtml(holder.mContentTextView)
                .setOnLinkClickListener(mOnLinkClickListener);

        holder.mVoteImageView.setEnabled(!holder.mItem.getMeta().getVote().isVoted());
        holder.mVoteCountTextView.setText(String.valueOf(holder.mItem.getMeta().getVote().getCount()));

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
        @BindView(R.id.reply) ImageView mReplyImageView;
        @BindView(R.id.vote) ImageView mVoteImageView;
        @BindView(R.id.vote_count) TextView mVoteCountTextView;
        public Comment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.avatar, R.id.author, R.id.reply, R.id.vote})
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }

            switch (v.getId()) {
                case R.id.avatar:
                case R.id.author:
                    mListener.openPage(mItem.getMeta().getReplier().getUrl(), null);
                    break;

                case R.id.reply:
                    mListener.onAt(mItem.getMeta().getReplier().getUsername());
                    break;

                case R.id.vote:
                    mListener.onVote(mItem.getMeta().getReplier().getUsername(), mItem.getMeta().getVote().getUrl(), new OnResponseListener<Boolean>() {
                        @Override
                        public void onSucceed(Boolean succeed) {
                            Comment.Vote vote = mItem.getMeta().getVote();
                            vote.setVoted(true);
                            if (succeed) {
                                vote.setCount(vote.getCount() + 1);
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String msg) {

                        }
                    });
                    break;

                default:
                    break;
            }
        }

        @OnLongClick({R.id.avatar, R.id.author})
        public boolean onLongClick(View v) {
            if (mListener == null) {
                return false;
            }

            switch (v.getId()) {
                case R.id.avatar:
                case R.id.author:
                    mListener.onAt(mItem.getMeta().getReplier().getUsername());
                    return true;

                default:
                    break;
            }

            return false;
        }
    }
}

package org.mazhuang.guanggoo.notifications;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.Notification;
import org.mazhuang.guanggoo.util.GlideUtil;
import org.mazhuang.guanggoo.util.MyHtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mazhuang
 * @date 2018/8/19
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notification> mData;
    private final FragmentCallBack mListener;

    public NotificationsAdapter(FragmentCallBack listener) {
        mListener = listener;
    }

    public void setData(List<Notification> data) {
        if (mData != data) {
            mData = data;
            notifyDataSetChanged();
        }
    }

    public void addData(List<Notification> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public boolean isFilled() {
        return (mData != null && mData.size() > 0);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.mTitleTextView.getContext();
        holder.mItem = mData.get(position);
        holder.mTitleTextView.setText(mData.get(position).getTopic().getTitle());
        MyHtmlHttpImageGetter imageGetter = new MyHtmlHttpImageGetter(holder.mContentTextView);
        imageGetter.enableCompressImage(true, 30);
        holder.mContentTextView.setHtml(holder.mItem.getContent(), imageGetter);
        if (getItemViewType(position) == Notification.TYPE_MENTIONED) {
            holder.mTypeTextView.setText(context.getString(R.string.mentioned_you));
        } else {
            holder.mTypeTextView.setText(context.getString(R.string.reply_you));
        }
        holder.mAuthorTextView.setText(holder.mItem.getUser().getUsername());
        GlideUtil.loadImage(holder.mAvatarImageView, holder.mItem.getUser().getAvatar());
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) ImageView mAvatarImageView;
        @BindView(R.id.author) TextView mAuthorTextView;
        @BindView(R.id.content) HtmlTextView mContentTextView;
        @BindView(R.id.type) TextView mTypeTextView;
        @BindView(R.id.title) TextView mTitleTextView;

        public Notification mItem;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        @OnClick({R.id.title, R.id.avatar, R.id.author})
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }

            switch (v.getId()) {
                case R.id.author:
                case R.id.avatar:
                    mListener.openPage(mItem.getUser().getUrl(), null);
                    break;

                case R.id.title:
                    mListener.openPage(mItem.getTopic().getUrl(), null);
                    break;

                default:
                    break;
            }
        }
    }
}

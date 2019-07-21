package org.mazhuang.guanggoo.userprofile.block;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mazhuang
 * @date 2019-07-21
 */
public class BlockedUserListAdapter extends RecyclerView.Adapter<BlockedUserListAdapter.ViewHolder> {

    private List<UserProfile> mData;
    private final FragmentCallBack mListener;

    public BlockedUserListAdapter(FragmentCallBack listener) {
        mListener = listener;
    }

    public void setData(List<UserProfile> data) {
        if (data == mData) {
            return;
        }

        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BlockedUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blocked_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedUserListAdapter.ViewHolder holder, int position) {
        holder.mItem = mData.get(position);
        holder.mUsernameTextView.setText(holder.mItem.getUsername());
        GlideUtil.loadImage(holder.mAvatarImageView, holder.mItem.getAvatar());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public boolean isFilled() {
        return (mData != null && mData.size() > 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UserProfile mItem;
        @BindView(R.id.avatar) ImageView mAvatarImageView;
        @BindView(R.id.username) TextView mUsernameTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.root})
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.root:
                    mListener.openPage(mItem.getUrl(), null);
                    break;

                default:
                    break;
            }
        }
    }
}

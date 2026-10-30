package org.mazhuang.guanggoo.search;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.data.entity.SearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author mazhuang
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SearchResult result);
    }

    private final List<SearchResult> mData = new ArrayList<>();
    private final OnItemClickListener mListener;

    public SearchResultAdapter(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setData(List<SearchResult> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<SearchResult> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        int start = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    public boolean isFilled() {
        return !mData.isEmpty();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchResult item = mData.get(position);

        holder.mTitleTextView.setText(item.getTitle());
        holder.mMetaTextView.setText(buildMeta(item));

        if (TextUtils.isEmpty(item.getExcerptHtml())) {
            holder.mExcerptTextView.setVisibility(View.GONE);
        } else {
            holder.mExcerptTextView.setVisibility(View.VISIBLE);
            holder.mExcerptTextView.setText(HtmlCompat.fromHtml(
                    item.getExcerptHtml().replace("<mark>", "<b>").replace("</mark>", "</b>"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        });
    }

    private String buildMeta(SearchResult item) {
        List<String> parts = new ArrayList<>();
        if (!TextUtils.isEmpty(item.getNodeTitle())) {
            parts.add(item.getNodeTitle());
        }
        if (!TextUtils.isEmpty(item.getAuthor())) {
            parts.add(item.getAuthor());
        }
        if (!TextUtils.isEmpty(item.getDate())) {
            parts.add(item.getDate());
        }
        if (!TextUtils.isEmpty(item.getHitType())) {
            parts.add(item.getHitType());
        }
        return TextUtils.join(" · ", parts);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView mTitleTextView;
        @BindView(R.id.meta) TextView mMetaTextView;
        @BindView(R.id.excerpt) TextView mExcerptTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

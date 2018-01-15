package org.mazhuang.guanggoo.nodescloud;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.NodeCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public class NodesCloudAdapter extends RecyclerView.Adapter<NodesCloudAdapter.ViewHolder> {

    private List<NodeCategory> mData;

    private FragmentCallBack mListener;

    private OnNodeClickListener mNodeClickListener;

    public NodesCloudAdapter(FragmentCallBack listener, OnNodeClickListener nodeClickListener) {
        mListener = listener;
        mNodeClickListener = nodeClickListener;
    }

    public void setData(List<NodeCategory> data) {
        if (mData == data) {
            return;
        }

        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public NodesCloudAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nodes_cloud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NodesCloudAdapter.ViewHolder holder, int position) {
        final NodeCategory category = mData.get(position);
        holder.mTitleTextView.setText(category.getLabel());
        holder.mNodesGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return (category.getNodes() == null) ? 0 : category.getNodes().size();
            }

            @Override
            public Object getItem(int position) {
                return category.getNodes().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ChildViewHolder childViewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_nodes_cloud, null);
                    childViewHolder = new ChildViewHolder(convertView);
                    convertView.setTag(childViewHolder);
                } else {
                    childViewHolder = (ChildViewHolder) convertView.getTag();
                }
                childViewHolder.mTitleTextView.setText(category.getNodes().get(position).getTitle());
                return convertView;
            }
        });
        holder.mNodesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    Node node = category.getNodes().get(position);
                    if (mNodeClickListener != null) {
                        mNodeClickListener.onNodeClick(node);
                    } else {
                        mListener.openPage(node.getUrl(), node.getTitle());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mData == null ) ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView mTitleTextView;
        @BindView(R.id.nodes) GridView mNodesGridView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.title) TextView mTitleTextView;
        ChildViewHolder(View root) {
            ButterKnife.bind(this, root);
        }
    }
}

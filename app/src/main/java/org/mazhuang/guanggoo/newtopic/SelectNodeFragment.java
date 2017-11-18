package org.mazhuang.guanggoo.newtopic;

import android.text.TextUtils;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.nodescloud.NodesCloudFragment;
import org.mazhuang.guanggoo.router.annotations.FinishWhenCovered;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

/**
 * Created by mazhuang on 2017/11/18.
 */

@FinishWhenCovered
public class SelectNodeFragment extends NodesCloudFragment {
    @Override
    public void onNodeClick(Node node) {
        String nodeCode = UrlUtil.getNodeCode(node.getUrl());
        if (mListener != null && !TextUtils.isEmpty(nodeCode)) {
            mListener.openPage(String.format(ConstantUtil.NEW_TOPIC_BASE_URL, nodeCode), getString(R.string.new_topic));
        }
    }
}

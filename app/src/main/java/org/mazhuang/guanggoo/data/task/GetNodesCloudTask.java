package org.mazhuang.guanggoo.data.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Node;
import org.mazhuang.guanggoo.data.entity.NodeCategory;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 * @date 2017/9/28
 */

public class GetNodesCloudTask extends BaseTask<List<NodeCategory>> {
    public GetNodesCloudTask(OnResponseListener<List<NodeCategory>> listener) {
        super(listener);
    }

    @Override
    public void run() {
        Document doc;

        try {
            doc = get(ConstantUtil.NODES_CLOUD_URL);
        } catch (IOException e) {
            e.printStackTrace();
            failedOnUI(e.getMessage());
            return;
        }

        Elements nodesCategoryList = doc.select("div.nodes-cloud ul li");

        if (nodesCategoryList.isEmpty()) {
            failedOnUI("找不到节点列表");
            return;
        }

        List<NodeCategory> nodesCloud = new ArrayList<>();

        for (Element element : nodesCategoryList) {
            Elements labelElement = element.select("label");
            Elements nodesElement = element.select("span.nodes a");
            if (!labelElement.isEmpty() && !nodesElement.isEmpty()) {
                List<Node> nodes = new ArrayList<>();
                for (Element nodeElement : nodesElement) {
                    Node node = new Node();
                    node.setTitle(nodeElement.text());
                    node.setUrl(nodeElement.absUrl("href"));

                    nodes.add(node);
                }

                if (!nodes.isEmpty()) {
                    NodeCategory category = new NodeCategory();
                    category.setLabel(labelElement.text());
                    category.setNodes(nodes);

                    nodesCloud.add(category);
                }
            }
        }

        if (!nodesCloud.isEmpty()) {
            successOnUI(nodesCloud);
        } else {
            failedOnUI("节点列表为空");
        }
    }
}

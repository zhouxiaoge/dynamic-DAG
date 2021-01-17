package com.zhouxiaoge.dag.node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月14日 13点45分
 */
public class NodeTest {
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        Node node1 = new Node();
        node1.setNodeName("node1");
        node1.setNodeType("PrintTaskJob");

        Node node2 = new Node();
        node2.setNodeName("node2");
        node2.setNodeType("RDBMSTaskJob");

        Node node3 = new Node();
        node3.setNodeName("node2");
        node3.setNodeType("SumTaskJob");

        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        // nodes.stream().map(node -> {
        //     new
        // })

    }
}

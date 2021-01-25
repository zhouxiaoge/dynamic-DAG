package com.zhouxiaoge.dag.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月25日 22点33分
 */
public class NodeUtils {
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        Node print = new Node();
        print.setNodeName("Node1");
        print.setNodeType("print-task-job");
        print.setNodeData(new HashMap<>());


        Node sum = new Node();
        sum.setNodeName("Node2");
        sum.setNodeType("sum-task-job");
        sum.setNodeData(new HashMap<>());


        Node rdbms = new Node();
        rdbms.setNodeName("Node3");
        rdbms.setNodeType("rdbms-task-job");
        rdbms.setNodeData(new HashMap<>());

        nodes.add(print);
        nodes.add(sum);
        nodes.add(rdbms);
        System.out.println(nodes);

        List<Line> lines = new ArrayList<>();
        Line line1 = new Line();
        line1.setNodeName("line1");
        line1.setNodeType("line-task-job");
        line1.setSourceId("Node1");
        line1.setTarget("Node2");

        Line line2 = new Line();
        line2.setNodeName("line2");
        line2.setNodeType("line-task-job");
        line2.setSourceId("Node2");
        line2.setTarget("Node3");

        lines.add(line1);
        lines.add(line2);
        System.out.println(lines);


    }
}

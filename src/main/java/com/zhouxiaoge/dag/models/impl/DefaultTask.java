package com.zhouxiaoge.dag.models.impl;

import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.node.Node;

import java.util.Map;

/**
 * @author 周小哥
 */
public class DefaultTask implements Task {

    private static final long serialVersionUID = 7251495568683098285L;

    private String id;

    private String name;

    private String type;

    private String[] dependants;

    private Map<String, Object> taskData;

    private Node node;

    public DefaultTask(String id, String name, String type, String[] dependants, Map<String, Object> taskData) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.dependants = dependants;
        this.taskData = taskData;
    }

    public DefaultTask(String id, String name, String type, String[] dependants, Map<String, Object> taskData, Node node) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.dependants = dependants;
        this.taskData = taskData;
        this.node = node;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String[] getDependants() {
        return dependants;
    }

    public Map<String, Object> getTaskData() {
        return taskData;
    }

    public Node getNode() {
        return node;
    }
}
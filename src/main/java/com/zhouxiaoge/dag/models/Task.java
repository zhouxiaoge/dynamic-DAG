package com.zhouxiaoge.dag.models;

import com.zhouxiaoge.dag.models.impl.DefaultTask;
import com.zhouxiaoge.dag.node.Node;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public interface Task extends Serializable {

    String getId();

    String getName();

    String getType();

    Map<String, Object> getTaskData();

    String[] getDependants();

    Node getNode();

    static Task of(String id, String name, String type) {
        return new DefaultTask(id, name, type, new String[0], Collections.emptyMap());
    }

    static Task of(String id, String name, String type, String[] dependants) {
        return new DefaultTask(id, name, type, dependants, Collections.emptyMap());
    }

    static Task of(String id, String name, String type, String[] dependants, Map<String, Object> taskData) {
        return new DefaultTask(id, name, type, dependants, taskData);
    }

    static Task of(String id, String name, String type, String[] dependants, Map<String, Object> taskData, Node node) {
        return new DefaultTask(id, name, type, dependants, taskData, node);
    }
}

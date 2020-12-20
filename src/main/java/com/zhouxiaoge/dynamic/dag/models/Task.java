package com.zhouxiaoge.dynamic.dag.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.zhouxiaoge.dynamic.dag.models.impl.DefaultTask;

public interface Task extends Serializable {

    String getId();

    String getName();

    String getType();

    Map<String, Object> getTaskData();

    String[] getDependants();

    static Task of(String id, String name, String type) {
        return new DefaultTask(id, name, type, new String[0], Collections.emptyMap());
    }

    static Task of(String id, String name, String type, String[] dependants) {
        return new DefaultTask(id, name, type, dependants, Collections.emptyMap());
    }

    static Task of(String id, String name, String type, String[] dependants, Map<String, Object> taskData) {
        return new DefaultTask(id, name, type, dependants, taskData);
    }
}

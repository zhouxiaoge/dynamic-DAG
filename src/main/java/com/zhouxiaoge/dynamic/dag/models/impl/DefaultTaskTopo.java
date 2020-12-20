package com.zhouxiaoge.dynamic.dag.models.impl;

import com.zhouxiaoge.dynamic.dag.models.Task;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultTaskTopo implements TaskTopo {

    private static final long serialVersionUID = -2770341704324132548L;

    private Task task;
    
    private int taskGroup;

    private String[] dependedTasks;
    
    @Override
    public String getTaskId() {
        return task.getId();
    }

    @Override
    public String[] getDependantTasks() {
        return task.getDependants();
    }
}
package com.zhouxiaoge.dynamic.dag.tasks.impl;

import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import com.zhouxiaoge.dynamic.dag.tasks.TaskMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultTaskMapper implements TaskMapper {

    private Map<String, Function<TaskTopo, Job>> map = new HashMap<>();

    public DefaultTaskMapper with(String taskType, Function<TaskTopo, Job> job) {
        map.put(taskType, job);
        return this;
    }

    @Override
    public Job mapTask(TaskTopo taskTopo) {
        return map.get(taskTopo.getTask().getType()).apply(taskTopo);
    }
}

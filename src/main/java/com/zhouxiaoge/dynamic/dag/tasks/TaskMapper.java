package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;

public interface TaskMapper {

    Job mapTask(TaskTopo taskTopo);
}

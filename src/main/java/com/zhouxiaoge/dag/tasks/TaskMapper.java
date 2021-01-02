package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskTopo;

public interface TaskMapper {

    Job mapTask(TaskTopo taskTopo);
}

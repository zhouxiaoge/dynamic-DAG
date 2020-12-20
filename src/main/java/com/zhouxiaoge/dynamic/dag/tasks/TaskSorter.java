package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.Batch;
import com.zhouxiaoge.dynamic.dag.models.Task;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import org.joo.promise4j.Promise;

public interface TaskSorter {

    Promise<Batch<TaskTopo>, Throwable> sortTasks(Batch<Task> batch);
}

package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskTopo;
import org.joo.promise4j.Promise;

public interface TaskSorter {

    Promise<Batch<TaskTopo>, Throwable> sortTasks(Batch<Task> batch);
}

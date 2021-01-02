package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import org.joo.promise4j.Promise;

import java.io.Serializable;

public interface TaskRouter extends Serializable, Component {

    Promise<Object, Throwable> routeJob(TaskNotifier notifier, String routingKey, Job job, TaskResult result);

    Promise<Object, Throwable> routeBatch(TaskNotifier notifier, String batchId);
}

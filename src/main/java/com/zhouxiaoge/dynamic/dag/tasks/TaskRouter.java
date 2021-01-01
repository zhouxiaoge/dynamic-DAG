package com.zhouxiaoge.dynamic.dag.tasks;

import java.io.Serializable;

import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskRouter extends Serializable, Component {

    Promise<Object, Throwable> routeJob(TaskNotifier notifier, String routingKey, Job job, TaskResult result);

    Promise<Object, Throwable> routeBatch(TaskNotifier notifier, String batchId);
}
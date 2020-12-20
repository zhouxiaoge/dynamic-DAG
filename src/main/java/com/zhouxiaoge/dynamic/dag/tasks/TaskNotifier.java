package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskNotifier {

    Promise<TaskResult, Exception> notifyJobComplete(String batchId, String taskId, TaskResult result);

    Promise<Object, Exception>  notifyBatchStart(String batchId);
}

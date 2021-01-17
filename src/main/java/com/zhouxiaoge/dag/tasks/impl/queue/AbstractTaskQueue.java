package com.zhouxiaoge.dag.tasks.impl.queue;

import com.zhouxiaoge.dag.models.*;
import com.zhouxiaoge.dag.models.impl.DefaultBatchExecution;
import com.zhouxiaoge.dag.models.impl.DefaultExecutionContext;
import com.zhouxiaoge.dag.models.impl.results.DefaultTaskResult;
import com.zhouxiaoge.dag.models.impl.results.FailedTaskResult;
import com.zhouxiaoge.dag.tasks.TaskNotifier;
import com.zhouxiaoge.dag.tasks.TaskQueue;
import com.zhouxiaoge.dag.tasks.TaskRouter;
import com.zhouxiaoge.dag.tasks.TaskStorage;
import lombok.AccessLevel;
import lombok.Getter;
import org.joo.promise4j.Promise;
import org.joo.promise4j.impl.CompletableDeferredObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTaskQueue implements TaskQueue, TaskNotifier {

    private TaskStorage storage;

    private TaskRouter router;

    public AbstractTaskQueue(TaskRouter router, TaskStorage storage) {
        this.router = router;
        this.storage = storage;
    }

    @Override
    public Promise<TaskResult, Throwable> runTasks(Batch<Job> batch) {
        CompletableDeferredObject<TaskResult, Throwable> deferred = new CompletableDeferredObject<>();
        DefaultBatchExecution batchExecution = new DefaultBatchExecution(deferred, batch);
        return storage.storeBatchExecution(batch.getId(), batchExecution).then(r -> {
            router.routeBatch(this, batch.getId());
            return deferred.promise();
        });
    }

    @Override
    public Promise<Object, Exception> notifyBatchStart(String batchId) {
        return storage.fetchBatchExecution(batchId).then(batchExecution -> {
            runJobs(batchExecution, batchId, batchExecution.getBatch().getBatch());
            return Promise.of(null);
        });
    }

    @Override
    public Promise<TaskResult, Exception> notifyJobComplete(String batchId, String taskId, TaskResult result) {
        if (result == null)
            result = new DefaultTaskResult(taskId, null);
        TaskResult theResult = result;
        return storage.fetchBatchExecution(batchId).then(batchExecution -> {
            Job job = batchExecution.mapTask(taskId);
            batchExecution.completeJob(job, theResult);
            if (!theResult.isSuccessful()) {
                return Promise.ofCause(new RuntimeException(theResult.getCause()));
            }
            runChildJobs(batchId, job, batchExecution);
            return Promise.of(theResult);
        });
    }

    protected void runJobs(BatchExecution batchExecution, String batchId, Job[] jobs) {
        for (Job job : jobs) {
            if (!batchExecution.canRun(job)) {
                continue;
            }
            String[] dependedTasks = job.getTaskTopo().getDependedTasks();

            Map<String, Object> map = new HashMap<>();

            for (String dependedTask : dependedTasks) {
                Map<String, TaskResult> completedJobs = batchExecution.getCompletedJobs();
                DefaultTaskResult taskResult = (DefaultTaskResult) completedJobs.get(dependedTask);
                Map<String, Object> data = taskResult.getData();
                map.putAll(data);
            }
            Map<String, Object> taskData = job.getTaskTopo().getTask().getTaskData();
            taskData.putAll(map);
            DefaultExecutionContext defaultExecutionContext = new DefaultExecutionContext(batchId, taskData);
            runJob(job, defaultExecutionContext);
        }
    }

    protected void runJob(Job job, ExecutionContext context) {
        String batchId = context.getBatchId();
        String taskId = job.getTaskTopo().getTaskId();
        doRunJob(job, context).then(result -> router.routeJob(this, batchId, job, result))
                .fail(ex -> router.routeJob(this, batchId, job, new FailedTaskResult(taskId, ex)));
    }

    protected void runChildJobs(String batchId, Job job, BatchExecution batchExecution) {
        Job[] jobs = Arrays.stream(job.getTaskTopo().getDependantTasks())
                .map(batchExecution::mapTask)
                .toArray(Job[]::new);
        runJobs(batchExecution, batchId, jobs);
    }

    public void onStop() {
        router.stop();
    }

    protected Promise<TaskResult, Exception> doRunJob(Job job, ExecutionContext context) {
        return Promise.of(null);
    }
}

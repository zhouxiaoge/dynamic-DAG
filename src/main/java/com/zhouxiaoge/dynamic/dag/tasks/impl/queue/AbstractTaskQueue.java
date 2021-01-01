package com.zhouxiaoge.dynamic.dag.tasks.impl.queue;

import com.zhouxiaoge.dynamic.dag.models.*;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatchExecution;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultExecutionContext;
import com.zhouxiaoge.dynamic.dag.models.impl.results.DefaultTaskResult;
import com.zhouxiaoge.dynamic.dag.models.impl.results.FailedTaskResult;
import com.zhouxiaoge.dynamic.dag.tasks.TaskNotifier;
import com.zhouxiaoge.dynamic.dag.tasks.TaskQueue;
import com.zhouxiaoge.dynamic.dag.tasks.TaskRouter;
import com.zhouxiaoge.dynamic.dag.tasks.TaskStorage;
import io.gridgo.framework.impl.NonameComponentLifecycle;
import lombok.AccessLevel;
import lombok.Getter;
import org.joo.promise4j.Promise;
import org.joo.promise4j.impl.CompletableDeferredObject;

import java.util.Arrays;
import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTaskQueue extends NonameComponentLifecycle implements TaskQueue, TaskNotifier {

    private TaskStorage storage;

    private TaskRouter router;

    public AbstractTaskQueue(TaskRouter router, TaskStorage storage) {
        this.router = router;
        this.storage = storage;
    }

    @Override
    public Promise<TaskResult, Throwable> runTasks(Batch<Job> batch) {
        var deferred = new CompletableDeferredObject<TaskResult, Throwable>();
        var batchExecution = new DefaultBatchExecution(deferred, batch);
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
        var theResult = result;
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
        for (var job : jobs) {
            if (!batchExecution.canRun(job)) {
                continue;
            }
            String[] dependedTasks = job.getTaskTopo().getDependedTasks();

            for (String dependedTask : dependedTasks) {
                Map<String, TaskResult> completedJobs = batchExecution.getCompletedJobs();
                TaskResult taskResult = completedJobs.get(dependedTask);
                System.out.println("上次执行结果--" + taskResult.getResult());
            }
            DefaultExecutionContext defaultExecutionContext = new DefaultExecutionContext(batchId, job.getTaskTopo().getTask().getTaskData());
            runJob(job, defaultExecutionContext);
        }
    }

    protected void runJob(Job job, ExecutionContext context) {
        String batchId = context.getBatchId();
        String taskId = job.getTaskTopo().getTaskId();
        doRunJob(job, context).then(result -> router.routeJob(this, batchId, job, result)) //
                .fail(ex -> router.routeJob(this, batchId, job, new FailedTaskResult(taskId, ex)));
    }

    protected void runChildJobs(String batchId, Job job, BatchExecution batchExecution) {
        Job[] jobs = Arrays.stream(job.getTaskTopo().getDependantTasks()) //
                .map(batchExecution::mapTask) //
                .toArray(Job[]::new);
        runJobs(batchExecution, batchId, jobs);
    }

    @Override
    protected void onStart() {
        router.start();
    }

    @Override
    protected void onStop() {
        router.stop();
    }

    protected Promise<TaskResult, Exception> doRunJob(Job job, ExecutionContext context) {
        return Promise.of(null);
    }
}

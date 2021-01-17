package com.zhouxiaoge.dag.models.impl;

import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.results.BatchTaskResult;
import com.zhouxiaoge.dag.models.impl.results.CanceledTaskResult;
import lombok.Getter;
import org.joo.promise4j.Deferred;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周小哥
 */
@Getter
public class DefaultBatchExecution implements BatchExecution {

    private Deferred<TaskResult, Throwable> deferred;

    private Batch<Job> batch;

    private Map<String, TaskResult> completedJobs = new HashMap<>();

    private Map<String, Job> taskMap = new HashMap<>();

    public DefaultBatchExecution(Deferred<TaskResult, Throwable> deferred, Batch<Job> batch) {
        this.deferred = deferred;
        this.batch = batch;
        buildTaskMap();
    }

    private void buildTaskMap() {
        for (Job job : batch.getBatch()) {
            taskMap.put(job.getTaskTopo().getTaskId(), job);
        }
    }

    @Override
    public boolean canRun(Job job) {
        String[] depended = job.getTaskTopo().getDependedTasks();
        if (depended.length == 0)
            return true;
        return Arrays.stream(depended).allMatch(this::hasCompleted);
    }

    private boolean hasCompleted(String task) {
        return completedJobs.containsKey(task) && completedJobs.get(task).isSuccessful();
    }

    @Override
    public void completeJob(Job job, TaskResult result) {
        completedJobs.put(job.getTaskTopo().getTaskId(), result);
        if (!result.isSuccessful()) {
            for (String child : job.getTaskTopo().getDependantTasks()) {
                completeJob(taskMap.get(child), new CanceledTaskResult(child));
            }
        }
        checkComplete();
    }

    protected void checkComplete() {
        if (isCompleted()) {
            deferred.resolve(new BatchTaskResult(batch.getId(), completedJobs));
        }
    }

    @Override
    public boolean isCompleted() {
        return completedJobs.size() == batch.getBatch().length;
    }

    @Override
    public Job mapTask(String taskId) {
        return taskMap.get(taskId);
    }
}

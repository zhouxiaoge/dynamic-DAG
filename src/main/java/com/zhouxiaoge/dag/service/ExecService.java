package com.zhouxiaoge.dag.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.util.StrUtil;
import com.zhouxiaoge.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dag.jobs.RDBMSTaskJob;
import com.zhouxiaoge.dag.jobs.SumTaskJob;
import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.DefaultTask;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gqzmy
 */
@Service
public class ExecService {


    FIFOCache<Object, Object> newFIFOCache = CacheUtil.newFIFOCache(3);

    public boolean asynExecTask(String dagKey, Map<String, Object> parameterMap) throws InterruptedException, PromiseException {
        List<Task> list = (List<Task>) newFIFOCache.get(dagKey);
        DefaultTaskMapper taskMapper = new DefaultTaskMapper()
                .with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new)
                .with("rdbms-task-job", RDBMSTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        submitter.start();
        list.forEach(task -> {
            if ("1".equals(task.getId())) {
                task.getTaskData().putAll(parameterMap);
            }
        });
        Task[] tasks = list.toArray(new Task[0]);
        Batch<Task> taskBatch = Batch.of("batchId-" + Thread.currentThread().getId(), tasks);
        Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
        TaskResult taskResult = taskResultThrowablePromise.get();
        submitter.stop();
        return taskResult.isSuccessful();
    }

    public void generateTaskDependant(String dagKey) {
        List<Task> list = new ArrayList<>();
        Task task1 = new DefaultTask("1", "task1", "print-task-job", new String[]{"2"}, new HashMap<>());
        Task task2 = new DefaultTask("2", "task2", "sum-task-job", new String[]{"3"}, new HashMap<>());
        Task task3 = new DefaultTask("3", "task5", "rdbms-task-job", new String[0], new HashMap<>());
        list.add(task1);
        list.add(task2);
        list.add(task3);
        newFIFOCache.put(StrUtil.isBlankIfStr(dagKey) ? "dagKey" : dagKey, list);
    }
}

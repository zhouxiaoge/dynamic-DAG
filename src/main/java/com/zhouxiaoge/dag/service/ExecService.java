package com.zhouxiaoge.dag.service;

import com.zhouxiaoge.dag.jobs.PrintTaskJob;
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

@Service
public class ExecService {

    public boolean syncDagExecTask(String variable, Map<String, Object> parameterMap) throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        submitter.start();
        Batch<Task> taskBatch = Batch.of("batchId-" + variable,
                Task.of("1", "task1", "print-task-job", new String[]{"2"}, parameterMap),
                Task.of("2", "task2", "sum-task-job", new String[]{"3"}),
                Task.of("3", "task3", "print-task-job", new String[]{"4"}),
                Task.of("4", "task4", "sum-task-job", new String[]{"5"}),
                Task.of("5", "task5", "print-task-job", new String[0]));
        Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
        TaskResult taskResult = taskResultThrowablePromise.get();
        submitter.stop();
        return taskResult.isSuccessful();
    }


    Map<String, List<Task>> map = new HashMap<>();

    public boolean asynExecTask(String variable, Map<String, Object> parameterMap) throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        submitter.start();
        List<Task> list = map.get(variable);
        list.forEach(task -> {
            if (task.getId().equals("1")) {
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

    public void generateTaskDependant() {
        List<Task> list = new ArrayList<>();
        Task task1 = new DefaultTask("1", "task1", "print-task-job", new String[]{"2"}, new HashMap<>());
        Task task2 = new DefaultTask("2", "task2", "sum-task-job", new String[]{"3"}, new HashMap<>());
        Task task3 = new DefaultTask("3", "task3", "print-task-job", new String[]{"4"}, new HashMap<>());
        Task task4 = new DefaultTask("4", "task4", "sum-task-job", new String[]{"5"}, new HashMap<>());
        Task task5 = new DefaultTask("5", "task5", "print-task-job", new String[0], new HashMap<>());
        list.add(task1);
        list.add(task2);
        list.add(task3);
        list.add(task4);
        list.add(task5);
        map.put("zhouxiaoge", list);
    }
}

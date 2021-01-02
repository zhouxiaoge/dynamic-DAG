package com.zhouxiaoge.dynamic.dag.jobs;

import com.zhouxiaoge.dynamic.dag.models.Batch;
import com.zhouxiaoge.dynamic.dag.models.Task;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dynamic.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dynamic.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;

import java.util.Collections;

public class ExecService {

    public boolean execTask(String variable) throws InterruptedException, PromiseException {
        DefaultTaskMapper taskMapper = new DefaultTaskMapper().with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new);
        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);
        submitter.start();
        Batch<Task> taskBatch = Batch.of("batchId-" + variable,
                Task.of("1", "task1", "print-task-job", new String[]{"2"}, Collections.singletonMap("key", variable)),
                Task.of("2", "task2", "sum-task-job", new String[]{"3"}),
                Task.of("3", "task3", "print-task-job", new String[]{"4"}),
                Task.of("4", "task4", "sum-task-job", new String[]{"5"}),
                Task.of("5", "task5", "print-task-job", new String[0]));
        Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
        System.out.println("----------------------------" + variable + "----------------------------");
        TaskResult taskResult = taskResultThrowablePromise.get();
        System.out.println("----------------------------" + variable + "----------------------------");
        submitter.stop();
        return taskResult.isSuccessful();
    }
}

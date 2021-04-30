package com.zhouxiaoge.dag.test;

import cn.hutool.core.util.ObjectUtil;
import com.zhouxiaoge.dag.jobs.PrintTaskJob;
import com.zhouxiaoge.dag.jobs.RDBMSTaskJob;
import com.zhouxiaoge.dag.jobs.SumTaskJob;
import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.impl.DefaultTask;
import com.zhouxiaoge.dag.node.Node;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskMapper;
import com.zhouxiaoge.dag.tasks.impl.DefaultTaskSubmitter;
import com.zhouxiaoge.dag.tasks.impl.queue.PooledTaskRunner;
import com.zhouxiaoge.dag.tasks.impl.routers.HashedTaskRouter;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.joo.promise4j.Promise;
import org.joo.promise4j.PromiseException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionSequenceTest {

    @Test
    public void testCircularDependency() throws InterruptedException, PromiseException {
        List<Task> list = new ArrayList<>();
        Node node = new Node();
        Map<String, Object> map = new HashMap<>();
        map.put("variable", "variable—value");

        node.setNodeData(map);

        Task task1 = new DefaultTask("1", "task1", "print-task-job", new String[]{"2"}, new HashMap<>(16), node);
        Task task2 = new DefaultTask("2", "task2", "sum-task-job", new String[]{"3"}, new HashMap<>(16), node);
        Task task3 = new DefaultTask("3", "task3", "print-task-job", new String[]{"4"}, new HashMap<>(16), node);
        Task task4 = new DefaultTask("4", "task4", "rdbms-task-job", new String[]{"5"}, new HashMap<>(16), node);
        Task task5 = new DefaultTask("5", "task5", "print-task-job", new String[0], new HashMap<>(16), node);
        list.add(task1);
        list.add(task2);
        list.add(task3);
        list.add(task4);
        list.add(task5);

        DefaultTaskMapper taskMapper = new DefaultTaskMapper()
                .with("print-task-job", PrintTaskJob::new)
                .with("sum-task-job", SumTaskJob::new)
                .with("rdbms-task-job", RDBMSTaskJob::new);

        MemBasedTaskStorage taskStorage = new MemBasedTaskStorage();
        HashedTaskRouter taskRouter = new HashedTaskRouter(2);
        PooledTaskRunner taskRunner = new PooledTaskRunner(16, taskRouter, taskStorage);
        DefaultTaskSubmitter submitter = new DefaultTaskSubmitter(taskRunner, taskMapper);

        List<Task> newTaskList = ObjectUtil.cloneByStream(list);

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("ID", "1");

        Task[] tasks = newTaskList.stream().peek(task -> task.getTaskData().putAll(parameterMap)).toArray(Task[]::new);
        Batch<Task> taskBatch = Batch.of("batchId-1", tasks);
        Promise<TaskResult, Throwable> taskResultThrowablePromise = submitter.submitTasks(taskBatch);
        TaskResult taskResult = taskResultThrowablePromise.get();
        boolean successful = taskResult.isSuccessful();
        System.out.println(successful);

    }


}

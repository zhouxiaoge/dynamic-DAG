package com.zhouxiaoge.dag.tasks.impl;

import com.zhouxiaoge.dag.models.*;
import com.zhouxiaoge.dag.models.impl.DefaultBatch;
import com.zhouxiaoge.dag.tasks.TaskMapper;
import com.zhouxiaoge.dag.tasks.TaskQueue;
import com.zhouxiaoge.dag.tasks.TaskSorter;
import com.zhouxiaoge.dag.tasks.TaskSubmitter;
import lombok.AllArgsConstructor;
import org.joo.promise4j.Promise;

import java.util.Arrays;

@AllArgsConstructor
public class DefaultTaskSubmitter implements TaskSubmitter {

    private final TaskSorter taskSorter;

    private final TaskQueue taskRunner;

    private final TaskMapper taskMapper;

    public DefaultTaskSubmitter(TaskQueue taskRunner, TaskMapper taskMapper) {
        this(new DAGTaskSorter(), taskRunner, taskMapper);
    }

    @Override
    public Promise<TaskResult, Throwable> submitTasks(Batch<Task> batch) {
        return taskSorter.sortTasks(batch)
                .map(this::mapTasks)
                .then(taskRunner::runTasks);
    }

    private Batch<Job> mapTasks(Batch<TaskTopo> batch) {
        Job[] jobs = Arrays.stream(batch.getBatch())
                .map(taskMapper::mapTask)
                .toArray(Job[]::new);
        return new DefaultBatch<>(batch.getId(), jobs);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

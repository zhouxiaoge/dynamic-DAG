package com.zhouxiaoge.dynamic.dag.tasks.impl;

import com.zhouxiaoge.dynamic.dag.models.*;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatch;
import com.zhouxiaoge.dynamic.dag.tasks.TaskMapper;
import com.zhouxiaoge.dynamic.dag.tasks.TaskQueue;
import com.zhouxiaoge.dynamic.dag.tasks.TaskSorter;
import com.zhouxiaoge.dynamic.dag.tasks.TaskSubmitter;
import io.gridgo.framework.impl.AbstractComponentLifecycle;
import lombok.AllArgsConstructor;
import org.joo.promise4j.Promise;

import java.util.Arrays;

@AllArgsConstructor
public class DefaultTaskSubmitter extends AbstractComponentLifecycle implements TaskSubmitter {

    private final TaskSorter taskSorter;

    private final TaskQueue taskRunner;

    private final TaskMapper taskMapper;

    public DefaultTaskSubmitter(TaskQueue taskRunner, TaskMapper taskMapper) {
        this(new DAGTaskSorter(), taskRunner, taskMapper);
    }

    @Override
    public Promise<TaskResult, Throwable> submitTasks(Batch<Task> batch) {
        return taskSorter.sortTasks(batch) //
                         .map(this::mapTasks) //
                         .then(taskRunner::runTasks);
    }

    private Batch<Job> mapTasks(Batch<TaskTopo> batch) {
        var jobs = Arrays.stream(batch.getBatch()) //
                         .map(taskMapper::mapTask) //
                         .toArray(size -> new Job[size]);
        return new DefaultBatch<>(batch.getId(), jobs);
    }
    
    @Override
    protected void onStart() {
        taskRunner.start();
    }

    @Override
    protected void onStop() {
        taskRunner.stop();
    }

    @Override
    protected String generateName() {
        return "tasksubmitter.default";
    }
}

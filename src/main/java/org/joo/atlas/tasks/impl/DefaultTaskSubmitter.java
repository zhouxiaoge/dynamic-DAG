package org.joo.atlas.tasks.impl;

import java.util.Arrays;

import org.joo.atlas.models.Batch;
import org.joo.atlas.models.Job;
import org.joo.atlas.models.Task;
import org.joo.atlas.models.TaskResult;
import org.joo.atlas.models.TaskTopo;
import org.joo.atlas.models.impl.DefaultBatch;
import org.joo.atlas.tasks.TaskMapper;
import org.joo.atlas.tasks.TaskRunner;
import org.joo.atlas.tasks.TaskSorter;
import org.joo.atlas.tasks.TaskSubmitter;
import org.joo.promise4j.Promise;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultTaskSubmitter implements TaskSubmitter {

    private final TaskSorter taskSorter;

    private final TaskRunner taskRunner;

    private final TaskMapper taskMapper;
    
    public DefaultTaskSubmitter(TaskRunner taskRunner, TaskMapper taskMapper) {
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
    public void stop() {
        taskRunner.stop();
    }
}

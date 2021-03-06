package com.zhouxiaoge.dag.tasks.impl;

import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskTopo;
import com.zhouxiaoge.dag.models.impl.DefaultBatch;
import com.zhouxiaoge.dag.tasks.TaskSorter;
import com.zhouxiaoge.dag.tasks.impl.algorithm.DFSTopoSorting;
import org.joo.promise4j.Promise;

/**
 * @author zhouxiaoge
 */
public class DAGTaskSorter implements TaskSorter {

    @Override
    public Promise<Batch<TaskTopo>, Throwable> sortTasks(Batch<Task> batch) {
        try {
            Task[] tasks = batch.getBatch();
            DFSTopoSorting sorter = new DFSTopoSorting(tasks);
            TaskTopo[] sortedTasks = sorter.sort().topo();
            return Promise.of(new DefaultBatch<>(batch.getId(), sortedTasks));
        } catch (Exception ex) {
            return Promise.ofCause(ex);
        }
    }
}

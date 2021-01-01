package com.zhouxiaoge.dynamic.dag.tasks.impl;

import com.zhouxiaoge.dynamic.dag.models.Batch;
import com.zhouxiaoge.dynamic.dag.models.Task;
import com.zhouxiaoge.dynamic.dag.models.TaskTopo;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatch;
import com.zhouxiaoge.dynamic.dag.tasks.TaskSorter;
import com.zhouxiaoge.dynamic.dag.tasks.impl.algorithm.DFSTopoSorting;
import org.joo.promise4j.Promise;

public class DAGTaskSorter implements TaskSorter {

    @Override
    public Promise<Batch<TaskTopo>, Throwable> sortTasks(Batch<Task> batch) {
        try {
            var sorter = new DFSTopoSorting(batch.getBatch());
            var sortedTasks = sorter.sort().topo();
            return Promise.of(new DefaultBatch<>(batch.getId(), sortedTasks));
        } catch (Exception ex) {
            return Promise.ofCause(ex);
        }
    }
}

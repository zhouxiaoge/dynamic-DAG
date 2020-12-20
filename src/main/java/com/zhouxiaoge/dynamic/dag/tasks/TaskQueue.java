package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.Batch;
import com.zhouxiaoge.dynamic.dag.models.Job;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskQueue extends Component {

    Promise<TaskResult, Throwable> runTasks(Batch<Job> batch);
}

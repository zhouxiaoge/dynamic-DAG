package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskQueue extends Component {

    Promise<TaskResult, Throwable> runTasks(Batch<Job> batch);
}

package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.Batch;
import com.zhouxiaoge.dynamic.dag.models.Task;
import com.zhouxiaoge.dynamic.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskSubmitter extends Component {

    Promise<TaskResult, Throwable> submitTasks(Batch<Task> batch);
}

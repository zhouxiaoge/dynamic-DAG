package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskResult;
import org.joo.promise4j.Promise;

public interface TaskSubmitter extends Component {

    Promise<TaskResult, Throwable> submitTasks(Batch<Task> batch);
}

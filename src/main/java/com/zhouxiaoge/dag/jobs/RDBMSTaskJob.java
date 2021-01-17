package com.zhouxiaoge.dag.jobs;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.zhouxiaoge.dag.models.ExecutionContext;
import com.zhouxiaoge.dag.models.Job;
import com.zhouxiaoge.dag.models.TaskResult;
import com.zhouxiaoge.dag.models.TaskTopo;
import com.zhouxiaoge.dag.models.impl.results.DefaultTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.joo.promise4j.Promise;

import java.util.Map;

/**
 * @author 周小哥
 */
@Slf4j
public class RDBMSTaskJob implements Job {

    private static final long serialVersionUID = -2296589918933132292L;

    private TaskTopo taskTopo;

    @Override
    public TaskTopo getTaskTopo() {
        return taskTopo;
    }

    public RDBMSTaskJob(TaskTopo taskTopo) {
        this.taskTopo = taskTopo;
    }

    @Override
    public Promise<TaskResult, Exception> run(ExecutionContext context) {
        try {
            Map<String, Object> contextData = context.getContextData();
            Entity set = Entity.create("DAG_TEST")
                    .set("ID", contextData.get("ID"))
                    .set("NAME", contextData.get("NAME"))
                    .set("AGE", contextData.get("AGE"))
                    .set("THREAD_NAME", contextData.get("THREAD_NAME"))
                    .set("SEX", contextData.get("SEX"))
                    .set("DAG_ID", context.getBatchId());
            Db.use().insert(set);
            return Promise.of(new DefaultTaskResult(taskTopo.getTaskId(), null, contextData));

        } catch (Exception e) {
            log.error("执行异常", e);
            return Promise.ofCause(new RuntimeException("just failed"));
        }
    }
}

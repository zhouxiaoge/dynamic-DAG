package com.zhouxiaoge.dynamic.dag.tasks;

import com.zhouxiaoge.dynamic.dag.models.BatchExecution;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatchExecution;
import org.joo.promise4j.Promise;

public interface TaskStorage {

    Promise<BatchExecution, Exception> fetchBatchExecution(String batchId);

    Promise<Object, Exception> storeBatchExecution(String id, DefaultBatchExecution batchExecution);
}

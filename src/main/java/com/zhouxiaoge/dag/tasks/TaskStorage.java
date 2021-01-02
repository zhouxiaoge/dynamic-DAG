package com.zhouxiaoge.dag.tasks;

import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.models.impl.DefaultBatchExecution;
import org.joo.promise4j.Promise;

public interface TaskStorage {

    Promise<BatchExecution, Exception> fetchBatchExecution(String batchId);

    Promise<Object, Exception> storeBatchExecution(String id, DefaultBatchExecution batchExecution);
}

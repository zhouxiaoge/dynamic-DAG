package com.zhouxiaoge.dag.tasks.impl.storages;

import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.models.impl.DefaultBatchExecution;
import com.zhouxiaoge.dag.tasks.TaskStorage;
import org.joo.promise4j.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周小哥
 */
public class MemBasedTaskStorage implements TaskStorage {

    private Map<String, BatchExecution> executionMap = new ConcurrentHashMap<>();

    @Override
    public Promise<BatchExecution, Exception> fetchBatchExecution(String batchId) {
        return Promise.of(executionMap.get(batchId));
    }

    @Override
    public Promise<Object, Exception> storeBatchExecution(String id, DefaultBatchExecution batchExecution) {
        executionMap.put(id, batchExecution);
        return Promise.of(null);
    }
}

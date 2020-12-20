package com.zhouxiaoge.dynamic.dag.tasks.impl.storages;

import java.util.Map;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import com.zhouxiaoge.dynamic.dag.models.BatchExecution;
import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatchExecution;
import com.zhouxiaoge.dynamic.dag.tasks.TaskStorage;
import org.joo.promise4j.Promise;

public class MemBasedTaskStorage implements TaskStorage {

    private Map<String, BatchExecution> executionMap = new NonBlockingHashMap<>();

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

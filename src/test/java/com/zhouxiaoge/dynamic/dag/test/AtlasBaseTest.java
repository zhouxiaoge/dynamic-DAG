package com.zhouxiaoge.dynamic.dag.test;


import com.zhouxiaoge.dag.models.Batch;
import com.zhouxiaoge.dag.models.Task;

import java.util.Collections;

public class AtlasBaseTest {

    protected Batch<Task> createBatchWithCircularDependency() {
        Batch<Task> batch = Batch.of("circular-batch", //
                Task.of("4", "task4", "test-task", new String[]{"6"}, Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("2", "task2", "test-task", new String[]{"3"}, Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("3", "task3", "test-task", new String[]{"4", "5"},
                        Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("1", "task1", "test-task", new String[]{"2", "3"},
                        Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("5", "task5", "test-task", new String[]{"6"}, Collections.singletonMap("sleepTimeMs", 300L)), //
                Task.of("7", "task7", "test-task", new String[]{"2"}, Collections.singletonMap("sleepTimeMs", 100L)), //
                Task.of("6", "task6", "test-task", new String[]{"1"}, Collections.singletonMap("sleepTimeMs", 100L)), //
                Task.of("8", "task8", "test-task", new String[0], Collections.singletonMap("sleepTimeMs", 500L)));
        return batch;
    }

    protected Batch<Task> createBatch(String batchId) {
        Batch<Task> batch = Batch.of(batchId, //
                Task.of("4", "task4", "test-task", new String[]{"6"}, Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("2", "task2", "test-task", new String[]{"3"}, Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("3", "task3", "test-task", new String[]{"4", "5"},
                        Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("1", "task1", "test-task", new String[]{"2", "3"},
                        Collections.singletonMap("sleepTimeMs", 100L)),
                Task.of("5", "task5", "test-task", new String[]{"6"}, Collections.singletonMap("sleepTimeMs", 300L)), //
                Task.of("7", "task7", "test-task", new String[]{"2"}, Collections.singletonMap("sleepTimeMs", 100L)), //
                Task.of("6", "task6", "test-task", new String[0], Collections.singletonMap("sleepTimeMs", 100L)), //
                Task.of("8", "task8", "test-task", new String[0], Collections.singletonMap("sleepTimeMs", 500L)));
        return batch;
    }
}

package com.zhouxiaoge.dag.test;

import com.zhouxiaoge.dag.exec.DagExecutor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TaskTest {

    @Test
    public void syncTest() {
        DagExecutor dagExecutor = new DagExecutor();
        dagExecutor.generateTaskDependant("zhouxiaoge");
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("ID", i);
            map.put("NAME", "zhouxiaoge-" + i);
            map.put("AGE", (int) (Math.random() * 100));
            map.put("SEX", i % 2);
            map.put("THREAD_NAME", Thread.currentThread().getName());
            System.out.println(dagExecutor.synchronizationExecTask("zhouxiaoge", map));
        }
    }

    @Test
    public void asynTest() throws ExecutionException, InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
        DagExecutor dagExecutor = new DagExecutor();
        dagExecutor.generateTaskDependant("zhouxiaoge");

        // List<Map<String, Object>> list = new ArrayList<>();
        List<Future<Map<String, Object>>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Future<Map<String, Object>> submit = executorService.submit(() -> {
                Map<String, Object> map = new HashMap<>();
                map.put("ID", finalI);
                map.put("NAME", "zhouxiaoge-" + finalI);
                map.put("AGE", (int) (Math.random() * 100));
                map.put("SEX", finalI % 2);
                map.put("THREAD_NAME", Thread.currentThread().getName());
                Thread.sleep(1000);
                return dagExecutor.synchronizationExecTask("zhouxiaoge", map);
            });
            // list.add(submit.get());
            list.add(submit);

        }
        System.out.println("-------------------------------------------");
        // for (Map<String, Object> stringObjectMap : list) {
        //     System.out.println(stringObjectMap);
        // }
        for (Future<Map<String, Object>> mapFuture : list) {
            Map<String, Object> stringObjectMap = mapFuture.get();
            System.out.println(stringObjectMap);
        }
    }
}

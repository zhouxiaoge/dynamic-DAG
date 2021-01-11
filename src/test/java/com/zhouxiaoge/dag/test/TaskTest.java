package com.zhouxiaoge.dag.test;

import com.zhouxiaoge.dag.service.ExecService;
import org.joo.promise4j.PromiseException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskTest {

    @Test
    public void syncTest() throws PromiseException, InterruptedException {
        ExecService execService = new ExecService();
        execService.generateTaskDependant("dagKey");
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("ID", i);
            map.put("NAME", "zhouxiaoge-" + i);
            map.put("AGE", (int) (Math.random() * 100));
            map.put("SEX", i % 2);
            System.out.println(execService.asynExecTask("zhouxiaoge", map));
        }
    }

    @Test
    public void asynTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecService execService = new ExecService();
        execService.generateTaskDependant("dagKey");
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            int finalI = i;
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行");
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ID", finalI);
                    map.put("NAME", "zhouxiaoge-" + finalI);
                    map.put("AGE", (int) (Math.random() * 100));
                    map.put("SEX", finalI % 2);
                    System.out.println(execService.asynExecTask("zhouxiaoge", map));
                    Thread.sleep(1000);
                } catch (InterruptedException | PromiseException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

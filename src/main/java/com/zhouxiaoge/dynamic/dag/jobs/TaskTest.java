package com.zhouxiaoge.dynamic.dag.jobs;

import org.joo.promise4j.PromiseException;
import org.junit.Test;

public class TaskTest {

    @Test
    public void test() throws PromiseException, InterruptedException {
        ExecService execService = new ExecService();
        for (int i = 0; i < 10; i++) {
            System.out.println(execService.execTask(i + ""));
        }
    }
}
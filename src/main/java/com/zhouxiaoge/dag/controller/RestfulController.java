package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.exec.DagExecutor;
import org.joo.promise4j.PromiseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 周小哥
 * @date 2021年01月25日 22点01分
 */

@RequestMapping("/restful")
@RestController
public class RestfulController {

    private final DagExecutor dagExecutor;

    public RestfulController(DagExecutor dagExecutor) {
        this.dagExecutor = dagExecutor;
    }

    @GetMapping("/{dagKey}")
    public void restfulDag(@PathVariable("dagKey") String dagKey) throws PromiseException, InterruptedException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("ID", 1);
        parameterMap.put("NAME", "zhouxiaoge-" + 1);
        parameterMap.put("AGE", (int) (Math.random() * 100));
        parameterMap.put("SEX", 1);
        boolean restful = dagExecutor.asynExecTask(dagKey, parameterMap);
        System.out.println("-----------------");
        System.out.println(restful);
    }
}

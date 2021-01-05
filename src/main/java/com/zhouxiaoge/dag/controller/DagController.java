package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.service.ExecService;
import org.joo.promise4j.PromiseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DagController {

    private final ExecService execService;

    public DagController(ExecService execService) {
        this.execService = execService;
    }

    @GetMapping("/syncDag/{variable}")
    public String syncDag(@PathVariable("variable") String variable, @RequestParam("map") Map<String, Object> map) throws PromiseException, InterruptedException {
        boolean b = execService.syncDagExecTask(variable, map);
        return String.valueOf(b);
    }

    @GetMapping("/asynDag/{variable}")
    public String asynDag(@PathVariable("variable") String variable, @RequestParam("map") Map<String, Object> map) throws PromiseException, InterruptedException {
        boolean b = execService.asynExecTask(variable, map);
        return String.valueOf(b);
    }
}

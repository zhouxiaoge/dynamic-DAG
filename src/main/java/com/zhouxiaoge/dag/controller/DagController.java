package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.jobs.ExecService;
import org.joo.promise4j.PromiseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DagController {

    private final ExecService execService;

    public DagController(ExecService execService) {
        this.execService = execService;
    }


    @GetMapping("/{variable}")
    public String index(@PathVariable("variable") String variable) throws PromiseException, InterruptedException {
        boolean b = execService.execTask(variable);
        return String.valueOf(b);
    }
}

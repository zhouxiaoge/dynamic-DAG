package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.component.DagComponent;
import com.zhouxiaoge.dag.models.Task;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年1月9日22点52分
 */
@RestController
public class DagController {

    private final DagComponent dagComponent;

    public DagController(DagComponent dagComponent) {
        this.dagComponent = dagComponent;
    }

    /**
     * http://127.0.0.1:8080/actuator/health
     */
    @GetMapping("/start/{dagKey}")
    @Timed(value = "all.kafka", longTask = true)
    public List<Task> startDag(@PathVariable("dagKey") String dagKey) {
        dagComponent.startDag(dagKey);
        return DagCacheUtils.getDagTasksRelation(dagKey);
    }
}

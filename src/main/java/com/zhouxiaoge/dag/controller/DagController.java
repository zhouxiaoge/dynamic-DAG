package com.zhouxiaoge.dag.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.component.DagComponent;
import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 周小哥
 * @date 2021年1月9日22点52分
 */
@RequestMapping("/dag")
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
    @Timed(value = "start.dag", longTask = true)
    public List<Task> startDag(@PathVariable("dagKey") String dagKey) {
        dagComponent.startDag(dagKey);
        return DagCacheUtils.getDagTasksRelation(dagKey);
    }

    @GetMapping("/stop/{dagKey}")
    @Timed(value = "stop.dag", longTask = true)
    public List<Task> stopDag(@PathVariable("dagKey") String dagKey) {
        dagComponent.stopDag(dagKey);
        return DagCacheUtils.getDagTasksRelation(dagKey);
    }

    @GetMapping("/all")
    public Set<String> getDagAll() {
        return dagComponent.getDagAll();
    }

    @GetMapping("/storageSize/{dagKey}")
    public String getMemBasedTaskStorageSize(@PathVariable("dagKey") String dagKey) {
        MemBasedTaskStorage memBasedTaskStorage = DagCacheUtils.getMemBasedTaskStorage(dagKey);
        if (!ObjectUtil.isEmpty(memBasedTaskStorage)) {
            Map<String, BatchExecution> executionMap = memBasedTaskStorage.getExecutionMap();
            if (!executionMap.isEmpty()) {
                return String.valueOf(executionMap.size());
            }
        }
        return "null";
    }
}

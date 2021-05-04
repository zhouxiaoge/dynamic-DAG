package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.dto.RestfulDTO;
import com.zhouxiaoge.dag.exec.DagExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/invoking")
    public ResponseEntity<Map<String, Object>> restfulDag(@RequestBody RestfulDTO restfulDTO) {
        Map<String, Object> map = dagExecutor.synchronizationExecTask(restfulDTO.getDagKey(), restfulDTO.getParameterMap());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.annotation.BuiltNodeTypeList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月25日 23点14分
 */
@RequestMapping("/node")
@RestController
public class NodeController {

    @GetMapping("/all")
    public List<Object> getNodeList() {
        return BuiltNodeTypeList.list;
    }

}

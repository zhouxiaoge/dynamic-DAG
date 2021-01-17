package com.zhouxiaoge.dag.node;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 周小哥
 * @date 2021年01月14日 13点21分
 */
@Data
public class Node implements Serializable {

    private static final long serialVersionUID = -5090668307638634957L;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点依赖
     */
    private String[] depend;

    /**
     * 节点数据
     */
    private Map<String, Object> nodeData;

}

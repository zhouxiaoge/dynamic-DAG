package com.zhouxiaoge.dag.node;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周小哥
 * @date 2021年01月25日 22点39分
 */
@Data
public class Line implements Serializable {
    private static final long serialVersionUID = 1950778224862681831L;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 源点Id
     */
    private String sourceId;

    /**
     * 目标Id
     */
    private String target;
}

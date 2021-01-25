package com.zhouxiaoge.dag.annotation;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author 周小哥
 * @date 2021年01月25日 23点03分
 */
@Component
@Data
@NodeType("print-task-job")
public class Print2Job {

    private String nodeType = this.getClass().getAnnotation(NodeType.class).value();

}

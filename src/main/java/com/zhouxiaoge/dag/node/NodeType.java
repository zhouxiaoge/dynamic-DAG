package com.zhouxiaoge.dag.node;

import java.lang.annotation.*;

/**
 * @author 周小哥
 * @date 2021年01月14日 13点35分
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NodeType {
    String value();
}

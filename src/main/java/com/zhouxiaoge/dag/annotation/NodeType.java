package com.zhouxiaoge.dag.annotation;

import java.lang.annotation.*;

/**
 * @author 周小哥
 * @date 2021年01月25日 22点59分
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NodeType {

    String value();

}

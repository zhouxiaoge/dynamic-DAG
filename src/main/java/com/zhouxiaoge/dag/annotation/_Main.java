package com.zhouxiaoge.dag.annotation;

/**
 * @author 周小哥
 * @date 2021年01月25日 23点04分
 */
public class _Main {
    public static void main(String[] args) {
        Print2Job print2Job = new Print2Job();
        Class<? extends Print2Job> aClass = print2Job.getClass();
        if (aClass.isAnnotationPresent(NodeType.class)) {
            NodeType annotation = aClass.getAnnotation(NodeType.class);
            System.out.println(annotation.value());
        }
    }
}

package com.zhouxiaoge.dag.models;

public enum TaskResultStatus {

    /**
     * 执行完毕
     */
    FINISHED,
    /**
     * 执行失败
     */
    FAILED,
    /**
     * 取消执行
     */
    CANCELED;
}

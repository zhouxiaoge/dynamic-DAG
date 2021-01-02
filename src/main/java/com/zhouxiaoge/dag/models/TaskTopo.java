package com.zhouxiaoge.dag.models;

import java.io.Serializable;

public interface TaskTopo extends Serializable {

    String getTaskId();

    int getTaskGroup();

    Task getTask();

    String[] getDependantTasks();

    String[] getDependedTasks();
}

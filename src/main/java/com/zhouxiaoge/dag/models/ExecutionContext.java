package com.zhouxiaoge.dag.models;

import java.io.Serializable;
import java.util.Map;

public interface ExecutionContext extends Serializable {

    public String getBatchId();

    public Map<String, Object> getContextData();
}

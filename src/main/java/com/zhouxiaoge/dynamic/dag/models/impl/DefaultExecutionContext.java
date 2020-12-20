package com.zhouxiaoge.dynamic.dag.models.impl;

import java.util.Map;

import com.zhouxiaoge.dynamic.dag.models.ExecutionContext;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultExecutionContext implements ExecutionContext {

    private static final long serialVersionUID = -7344644340644341618L;

    private String batchId;
    
    private Map<String, Object> contextData;
}

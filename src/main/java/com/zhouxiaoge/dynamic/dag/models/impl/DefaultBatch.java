package com.zhouxiaoge.dynamic.dag.models.impl;

import com.zhouxiaoge.dynamic.dag.models.Batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultBatch<T> implements Batch<T> {

    private String id;
    
    private T[] batch;
}
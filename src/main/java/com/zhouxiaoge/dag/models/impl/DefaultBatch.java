package com.zhouxiaoge.dag.models.impl;

import com.zhouxiaoge.dag.models.Batch;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultBatch<T> implements Batch<T> {

    private String id;
    
    private T[] batch;
}
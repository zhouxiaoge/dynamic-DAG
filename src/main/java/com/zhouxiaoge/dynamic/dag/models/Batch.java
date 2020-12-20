package com.zhouxiaoge.dynamic.dag.models;

import com.zhouxiaoge.dynamic.dag.models.impl.DefaultBatch;

public interface Batch<T> {

    String getId();
    
    T[] getBatch();
    
    @SuppressWarnings("unchecked")
    static <T> Batch<T> of(String id, T...batch) {
        return new DefaultBatch<T>(id, batch);
    }
}

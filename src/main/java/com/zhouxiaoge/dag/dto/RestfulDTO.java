package com.zhouxiaoge.dag.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RestfulDTO implements Serializable {

    private String dagKey;

    private Map<String, Object> parameterMap;
}

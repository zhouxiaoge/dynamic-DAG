package com.zhouxiaoge.dag.models.impl;

import com.zhouxiaoge.dag.models.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DefaultTask implements Task {

    private static final long serialVersionUID = 7251495568683098285L;

    private String id;

    private String name;

    private String type;

    private String[] dependants;

    private Map<String, Object> taskData;
}
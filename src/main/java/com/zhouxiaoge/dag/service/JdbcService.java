package com.zhouxiaoge.dag.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JdbcService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> selectDagTest() {
        String sql = "SELECT  * FROM DAG_TEST";
        return jdbcTemplate.queryForList(sql);
    }
}

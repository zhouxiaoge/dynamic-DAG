package com.zhouxiaoge.dag.service;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class JdbcServiceTest {

    @Autowired
    private JdbcService jdbcService;


    @Test
    void selectDagTest() {
        List<Map<String, Object>> maps = jdbcService.selectDagTest();
        System.out.println(maps);
    }

    @Test
    void huToolDBTest() throws SQLException {
        Db.use().insert(Entity.create("DAG_TEST")
                .set("ID", "1")
                .set("NAME", "1")
                .set("AGE", "1")
                .set("SEX", "1")
        );
        List<Entity> dag_test = Db.use().findAll("DAG_TEST");
        System.out.println(dag_test);
    }
}
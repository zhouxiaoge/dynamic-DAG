package com.zhouxiaoge.dag;

import com.zhouxiaoge.dag.entity.UserEntity;
import com.zhouxiaoge.dag.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DagApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<UserEntity> userEntities = userMapper.selectList(null);
        System.out.println(userEntities);
    }

}

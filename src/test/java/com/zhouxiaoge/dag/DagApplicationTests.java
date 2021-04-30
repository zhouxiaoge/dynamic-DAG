package com.zhouxiaoge.dag;

import com.zhouxiaoge.dag.entity.UserEntity;
import com.zhouxiaoge.dag.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DagApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<UserEntity> userEntities = userMapper.selectList(null);
        System.out.println(userEntities);
    }

}

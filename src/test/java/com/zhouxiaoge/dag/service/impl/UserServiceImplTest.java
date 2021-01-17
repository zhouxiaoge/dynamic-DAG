package com.zhouxiaoge.dag.service.impl;

import com.zhouxiaoge.dag.entity.UserEntity;
import com.zhouxiaoge.dag.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月17日 20点38分
 */
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void getAllUser() {
        List<UserEntity> allUser = userService.getAllUser();
        System.out.println(allUser);
    }
}
package com.zhouxiaoge.dag.service.impl;

import com.zhouxiaoge.dag.entity.UserEntity;
import com.zhouxiaoge.dag.mapper.UserMapper;
import com.zhouxiaoge.dag.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月17日 20点32分
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserEntity> getAllUser() {
        return userMapper.selectList(null);
    }
}

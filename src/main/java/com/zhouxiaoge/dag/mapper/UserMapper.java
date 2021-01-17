package com.zhouxiaoge.dag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouxiaoge.dag.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 周小哥
 * @date 2021年01月17日 19点56分
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}

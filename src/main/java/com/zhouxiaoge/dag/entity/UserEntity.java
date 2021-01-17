package com.zhouxiaoge.dag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 周小哥
 * @date 2021年01月17日 19点55分
 */
@TableName("USER_INFO")
@Data
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 395820916143064306L;
    private Long id;

    private String name;

    private Integer age;

    private String email;
}

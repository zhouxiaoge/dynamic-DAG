package com.zhouxiaoge.dag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 周小哥
 * @date 2021年01月17日 20点27分
 */
@TableName("DAG_INFO")
@Data
public class DagEntity implements Serializable {

    private static final long serialVersionUID = 7564643898640316391L;
    private Long id;

    private String name;

    private Integer age;

    private String sex;

    private String ThreadName;

    private Double salary;

    private String dagId;

}

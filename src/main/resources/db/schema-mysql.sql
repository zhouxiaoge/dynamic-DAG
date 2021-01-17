DROP TABLE IF EXISTS DAG_INFO;

create table DAG_INFO
(
    ID          int            not null comment '主键D' primary key,
    NAME        varchar(255)   null comment '姓名',
    AGE         int            null comment '年龄',
    SEX         varchar(255)   null comment '性别',
    THREAD_NAME varchar(255)   null comment '线程名称',
    SALARY      decimal(10, 2) null comment '工资',
    Dag_ID      varchar(255)   null comment 'dagId'
);

DROP TABLE IF EXISTS USER_INFO;

CREATE TABLE USER_INFO
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

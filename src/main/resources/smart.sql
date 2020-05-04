-- auto-generated definition
create table device
(
    id               int auto_increment
        primary key,
    device_name      varchar(100)                        null,
    device_area      varchar(30)                         null,
    report_threshold int                                 null,
    create_time      timestamp default CURRENT_TIMESTAMP null,
    email            varchar(50)                         null
)
    charset = utf8mb4;
-- auto-generated definition
create table device_history
(
    id          int auto_increment
        primary key,
    name        varchar(50)                         null,
    start_time  varchar(30)                         null,
    end_time    varchar(30)                         null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    email       varchar(50)                         null,
    use_time    int                                 null,
    status      tinyint                             null comment '0异常 1正常',
    area        varchar(30)                         null
);
    charset = utf8mb4;
-- auto-generated definition
create table environment
(
    id               int auto_increment
        primary key,
    e_name           varchar(40)                         null,
    e_area           varchar(30)                         null,
    report_threshold double                              null,
    e_normal         double                              null,
    create_time      timestamp default CURRENT_TIMESTAMP null,
    email            varchar(50)                         null
)
     charset = utf8mb4;
-- auto-generated definition
create table environment_history
(
    id          int auto_increment
        primary key,
    update_time varchar(30)                         null,
    current     double                              null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    status      tinyint                             null,
    area        varchar(30)                         null,
    name        varchar(30)                         null,
    email       varchar(50)                         null
);
     charset = utf8mb4;
-- auto-generated definition
create table user
(
    u_name      varchar(100)                        null,
    id          int auto_increment
        primary key,
    password    varchar(100)                        null,
    email       varchar(100)                        null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    status      tinyint                             null comment '0未激活 1激活'
);
    charset = utf8mb4;



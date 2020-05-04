-- auto-generated definition
create table user
(
    u_name      varchar(100)                        null,
    id          int auto_increment
        primary key,
    password    varchar(100)                        null,
    email       varchar(100)                        null,
    create_time timestamp default CURRENT_TIMESTAMP null
);

alter table user
	add status tinyint null comment '0未激活 1激活';

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
    comment '设备表';

create table environment
(
	id int auto_increment,
	e_name varchar(40) null,
	e_area varchar(30) null,
	report_threshold double null,
	e_normal double null,
	create_time timestamp default now() null,
	email varchar(50) null,
	constraint environment_pk
		primary key (id)
)
comment '环境表';


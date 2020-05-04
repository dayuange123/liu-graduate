package com.ljy.graduate.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/29
 * Description:
 */
@Getter
@AllArgsConstructor
public enum ResponseMessage {

    SYS_ERROR(500, "系统内部错误"),
    USER_EXIST(401, "用户已经存在,换个邮箱注册"),
    USER_NOT_EXIST(402, "用户不存在"),
    USER_PASSWORD_NOT_TRUE(403,"密码错误"),
    USER_NOT_ACTIVE(404,"该账户未激活"),
    USER_OLD_PASSWORD_ERROR(405,"旧密码输入错误"),
    AREA_NAME_EXIST(10001,"该区域的此环境指标已经创建"),


    ;


    private int ec;

    private String em;

}

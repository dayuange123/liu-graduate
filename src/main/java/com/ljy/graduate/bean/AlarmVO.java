package com.ljy.graduate.bean;

import lombok.Data;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */


@Data
public class AlarmVO {
    private Integer time;
    private Integer count;
}

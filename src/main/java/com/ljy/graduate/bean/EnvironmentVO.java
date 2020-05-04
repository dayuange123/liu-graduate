package com.ljy.graduate.bean;

import lombok.Data;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Data
public class EnvironmentVO {

    private Integer id;

    private String name;

    private String area;

    private Double normal;

    private Double reportThreshold;

    private String createTime;
}

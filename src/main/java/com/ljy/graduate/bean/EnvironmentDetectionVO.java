package com.ljy.graduate.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
@Builder
public class EnvironmentDetectionVO {

    private Integer id;

    private String name;

    private Double current;

    private Double normal;

    private String area;

    private Double reportThreshold;

    private Boolean isError;

    private String updateTime;

    @Tolerate
    public EnvironmentDetectionVO() {
    }
}

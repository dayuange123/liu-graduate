package com.ljy.graduate.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Data
@Builder
public class DeviceDetectionVO {
    private Integer id;
    /**
     * min
     */
    private Long useTime;
    /**
     * 0关闭
     */
    private Integer status;


    private String startTime;

    private String endTime;

    private String reportThreshold;

    private  String name;
    private  String area;
    /**
     * 0 安全
     */
    private Boolean isError;

    @Tolerate
    public DeviceDetectionVO() {
    }


}

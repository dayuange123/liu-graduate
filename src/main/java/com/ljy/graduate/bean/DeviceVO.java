package com.ljy.graduate.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import java.util.Date;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Data
@Builder
public class DeviceVO {
    private Integer id;

    private String deviceName;

    private String deviceArea;

    private String reportThreshold;

    private String createTime;

    @Tolerate
    public DeviceVO() {
    }
}

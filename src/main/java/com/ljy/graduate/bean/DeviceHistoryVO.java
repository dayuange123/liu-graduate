package com.ljy.graduate.bean;

import com.ljy.graduate.entity.DeviceHistory;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
public class DeviceHistoryVO {
    private Integer id;

    private String name;


    private String area;

    private String startTime;

    private String endTime;

    private String createTime;

    private Integer useTime;

    private Integer status;

    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    public DeviceHistoryVO get(DeviceHistory deviceHistory){
        id=deviceHistory.getId();
        name=deviceHistory.getName();
        area=deviceHistory.getArea();
        startTime=deviceHistory.getStartTime();
        endTime=deviceHistory.getEndTime();
        createTime=format.format(deviceHistory.getCreateTime());
        useTime=deviceHistory.getUseTime();
        status=deviceHistory.getStatus();
        return this;
    }
}

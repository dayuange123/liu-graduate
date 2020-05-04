package com.ljy.graduate.bean;

import com.ljy.graduate.entity.DeviceHistory;
import com.ljy.graduate.entity.EnvironmentHistory;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
public class EnvironmentHistoryVO {
    private Integer id;

    private String name;

    private String area;


    private String updateTime;

    private String createTime;

    private Double current;

    private Integer status;

    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    public EnvironmentHistoryVO get(EnvironmentHistory environmentHistory){
        id=environmentHistory.getId();
        name=environmentHistory.getName();
        area=environmentHistory.getArea();
        updateTime=environmentHistory.getUpdateTime();
        createTime=format.format(environmentHistory.getCreateTime());
        status=environmentHistory.getStatus();
        current=environmentHistory.getCurrent();
        return this;
    }
}

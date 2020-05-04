package com.ljy.graduate.service;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.DataVO;
import com.ljy.graduate.common.Response;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
public interface DataService {
    Response<DataVO> getData(String email);

    Response<AlarmDataVO> getDeviceAlarmData(String email);

    Response<AlarmDataVO> getEnvironmentAlarmData(String email);
}

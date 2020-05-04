package com.ljy.graduate.service;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.AlarmVO;
import com.ljy.graduate.bean.DeviceHistoryVO;
import com.ljy.graduate.bean.HistorySearch;
import com.ljy.graduate.common.PageResponse;
import com.ljy.graduate.entity.DeviceHistory;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
public interface DeviceHistoryService {
    PageResponse<DeviceHistoryVO> findAllHistoryPageable(String email, Integer page);

    PageResponse<DeviceHistoryVO> findHistoryPageable(String email, HistorySearch historySearch);


    void addHistoryData(DeviceHistory deviceHistory);

    Integer getAlarmCount(String email);

    AlarmDataVO getDeviceAlarmData(String email);
}

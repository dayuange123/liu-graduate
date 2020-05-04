package com.ljy.graduate.service;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.EnvironmentHistoryVO;
import com.ljy.graduate.bean.HistorySearch;
import com.ljy.graduate.common.PageResponse;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.entity.EnvironmentHistory;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
public interface EnvironmentHistoryService {
    PageResponse<EnvironmentHistoryVO> findHistoryPageable(String email, HistorySearch historySearch);

    void addData(EnvironmentHistory environmentHistory);

    Integer getAlarmCount(String email);

    AlarmDataVO getAlarmData(String email);
}

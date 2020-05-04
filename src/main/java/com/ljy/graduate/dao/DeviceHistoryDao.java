package com.ljy.graduate.dao;

import com.ljy.graduate.bean.AlarmVO;
import com.ljy.graduate.entity.DeviceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Repository("deviceHistoryDao")
public interface DeviceHistoryDao extends JpaRepository<DeviceHistory, Integer> {
    Page<DeviceHistory> findAll(Specification<DeviceHistory> specification, Pageable pageable);

    int countByEmailAndStatus(String email, Integer status);

    @Query(value = "SELECT DATE_FORMAT(create_time,'%H') as time, count(*) as count from device_history where status=0 and email=:email and create_time between :start AND :end GROUP BY time"
        , nativeQuery = true)

    List<Map<String,Object>> getDeviceAlarmData(String email, String start, String end);
}

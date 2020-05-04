package com.ljy.graduate.dao;

import com.ljy.graduate.entity.DeviceHistory;
import com.ljy.graduate.entity.EnvironmentHistory;
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
@Repository("environmentHistoryDao")
public interface EnvironmentHistoryDao extends JpaRepository<EnvironmentHistory, Integer> {
    Page<EnvironmentHistory> findAll(Specification<EnvironmentHistory> specification, Pageable pageable);

    int countByEmailAndStatus(String email, Integer status);

    @Query(value = "SELECT DATE_FORMAT(create_time,'%H') as time, count(*) as count from environment_history where status=0 and email=:email and create_time between :start AND :end GROUP BY time"
        , nativeQuery = true)
    List<Map<String, Object>> getAlarmData(String email, String start, String end);
}

package com.ljy.graduate.dao;

import com.ljy.graduate.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Repository("deviceDao")
public interface DeviceDao extends JpaRepository<Device, Integer> {

    List<Device> findAllByEmail(String email);

    int countByEmail(String email);

}

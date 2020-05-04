package com.ljy.graduate.dao;

import com.ljy.graduate.entity.Device;
import com.ljy.graduate.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Repository("environmentDao")
public interface EnvironmentDao extends JpaRepository<Environment, Integer> {
    List<Environment> findAllByEmail(String email);

    Environment findByEmailAndAreaAndName(String email, String area, String name);

    int countByEmail(String email);

}

package com.ljy.graduate.dao;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/29
 * Description:
 */
public interface RedisDao {


    String getDeviceDataByEmail(String email);


    void setDeviceDataByEmail(String email, String newData);

    void setEnvironmentDataByEmail(String email, String newData);


    String getEnvironmentDataByEmail(String email);
}

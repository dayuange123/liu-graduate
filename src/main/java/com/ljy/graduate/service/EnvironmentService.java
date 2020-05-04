package com.ljy.graduate.service;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.bean.EnvironmentVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.entity.Environment;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
public interface EnvironmentService {
    Response<Boolean> addEnvironment(String email, Environment environment);

    Response<List<EnvironmentVO>> getAllDevice(String email);

    void deleteEnvironment(String email, Integer id);
}

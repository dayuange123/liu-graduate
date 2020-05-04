package com.ljy.graduate.service;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.entity.Device;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
public interface DeviceService {
    Response<Boolean> addDevice(String email, Device device);

    Response<List<DeviceVO>> getAllDevice(String email);

    void deleteDevice(String email, Integer id);

}

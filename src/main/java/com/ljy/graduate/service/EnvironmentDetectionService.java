package com.ljy.graduate.service;

import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.bean.EnvironmentDetectionVO;
import com.ljy.graduate.common.Response;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
public interface EnvironmentDetectionService {
    Response<List<EnvironmentDetectionVO>> getAllData(String email);

    void changeEnvironmentData() throws MessagingException;

}

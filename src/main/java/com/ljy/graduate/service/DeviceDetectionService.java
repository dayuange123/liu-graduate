package com.ljy.graduate.service;

import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.common.Response;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
public interface DeviceDetectionService {

    Response<List<DeviceDetectionVO>> getAllDeviceData(String email) throws ParseException, MessagingException;

    Response<Boolean> operateDevice(String email, Integer deviceId, Integer status) throws ParseException, MessagingException;

}

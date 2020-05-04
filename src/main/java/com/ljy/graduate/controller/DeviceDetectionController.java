package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.service.DeviceDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description: 设备实施检测
 */
@RestController
@RequestMapping("/detection")
@Slf4j
public class DeviceDetectionController {

    @Resource(name = "deviceDetectionService")
    private DeviceDetectionService deviceDetectionService;
    @RequestMapping(value = "/getAllDeviceData", method = RequestMethod.POST)
    public Response<List<DeviceDetectionVO>> getAllDeviceData(@CookieValue("email") String email) {
        try {
            return deviceDetectionService.getAllDeviceData(email);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }


    /**
     * @param email
     * @param deviceId
     * @param status   0代表关闭 1代表打开
     * @return
     */
    @RequestMapping(value = "/operateDevice", method = RequestMethod.POST)
    public Response<Boolean> operateDevice(@CookieValue("email") String email, Integer deviceId, Integer status) {
        try {
            return deviceDetectionService.operateDevice(email, deviceId,status);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

}

package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.bean.EnvironmentDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.service.DeviceDetectionService;
import com.ljy.graduate.service.EnvironmentDetectionService;
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
@RequestMapping("/environment/detection")
@Slf4j
public class EnvironmentDetectionController {

    @Resource(name = "environmentDetectionService")
    private EnvironmentDetectionService environmentDetectionService;

    @RequestMapping(value = "/getAllData", method = RequestMethod.POST)
    public Response<List<EnvironmentDetectionVO>> getAllDeviceData(@CookieValue("email") String email) {
        try {
            return environmentDetectionService.getAllData(email);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }



}

package com.ljy.graduate.controller;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.DataVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;

import com.ljy.graduate.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class DataController {

    @Resource(name = "dataService")
    private DataService dataService;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public Response<DataVO> getData(@CookieValue("email") String email) {
        try {
            return dataService.getData(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }


    @RequestMapping(value = "/getDeviceAlarmData", method = RequestMethod.POST)
    public Response<AlarmDataVO> getDeviceAlarmData(@CookieValue("email") String email) {
        try {
            return dataService.getDeviceAlarmData(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/getEnvironmentAlarmData", method = RequestMethod.POST)
    public Response<AlarmDataVO> getEnvironmentAlarmData(@CookieValue("email") String email) {
        try {
            return dataService.getEnvironmentAlarmData(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }
}

package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.entity.Device;
import com.ljy.graduate.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController {
    @Resource(name = "deviceService")
    private DeviceService deviceService;


    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    public Response<Boolean> addDevice(@CookieValue("email") String email, Device device) {
        try {
            return deviceService.addDevice(email, device);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/getAllDevice", method = RequestMethod.POST)
    public Response<List<DeviceVO>> getAllDevice(@CookieValue("email") String email) {
        try {
            return deviceService.getAllDevice(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }


    @RequestMapping(value = "/deleteDevice", method = RequestMethod.GET)
    public void deleteDevice(@CookieValue("email") String email, Integer id, HttpServletResponse response, HttpServletRequest request) {
        try {
            deviceService.deleteDevice(email,id);
            response.sendRedirect(request.getContextPath() + "/me/device.html");

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }


}

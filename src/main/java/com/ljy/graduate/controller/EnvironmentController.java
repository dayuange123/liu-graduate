package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.bean.EnvironmentVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.entity.Device;
import com.ljy.graduate.entity.Environment;
import com.ljy.graduate.service.EnvironmentService;
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
@RequestMapping("/environment")
@Slf4j
public class EnvironmentController {
    @Resource(name = "environmentService")
    private EnvironmentService environmentService;

    @RequestMapping(value = "/addEnvironment", method = RequestMethod.POST)
    public Response<Boolean> addEnvironment(@CookieValue("email") String email, Environment environment) {
        try {
            return environmentService.addEnvironment(email, environment);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/getAllEnvironment", method = RequestMethod.POST)
    public Response<List<EnvironmentVO>> getAllEnvironment(@CookieValue("email") String email) {
        try {
            return environmentService.getAllDevice(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/deleteEnvironment", method = RequestMethod.GET)
    public void deleteDevice(@CookieValue("email") String email, Integer id, HttpServletResponse response, HttpServletRequest request) {
        try {
            environmentService.deleteEnvironment(email, id);
            response.sendRedirect(request.getContextPath() + "/me/environment.html");

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
}

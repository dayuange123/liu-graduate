package com.ljy.graduate.controller;

import com.ljy.graduate.bean.UserVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource(name = "userService")
    UserService userService;

    /**
     * 注册
     *
     * @param userVO 用户信息
     * @return success
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<Boolean> register(UserVO userVO) {
        try {
            return userService.register(userVO);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<Boolean> login(UserVO userVO, HttpServletResponse response) {
        try {
            Response<Boolean> res = userService.login(userVO);
            Cookie cookie = new Cookie("email", userVO.getEmail());
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            return res;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/activateUser", method = RequestMethod.GET)
    public String activateUser(String email) {
        try {
            Response<Boolean> response = userService.activateUser(email);
            if (response.getData()) {
                return "激活成功";
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return "激活失败";
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Response<Boolean> updatePassword(String oldPassword, String newPassword, @CookieValue("email") String email) {

        try {
            return userService.updatePassword(oldPassword, newPassword, email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }

    }

    @RequestMapping(value = "/getUserProfile", method = RequestMethod.POST)
    public Response<UserVO> getUserProfile(@CookieValue("email") String email) {
        try {
            return userService.getUserProfile(email);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }

    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public Response<UserVO> updateProfile(String email, String userName) {
        try {
            return userService.updateProfile(email, userName);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Response<>(ResponseMessage.SYS_ERROR);
        }

    }

    @RequestMapping(value = "/quitLogin", method = RequestMethod.GET)
    public void quitLogin(@CookieValue("email") String email, HttpServletResponse response, HttpServletRequest request) throws IOException {
        log.info("1233");
        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/me/login.html");
        }

        Cookie cookie = new Cookie("email", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect(request.getContextPath() + "/me/login.html");
    }
}

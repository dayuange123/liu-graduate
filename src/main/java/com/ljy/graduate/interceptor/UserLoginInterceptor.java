package com.ljy.graduate.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description:
 * author:cb
 * datetime:2019年7月28日  下午6:05:42
 */
@Component
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    return true;
                }
            }
        }
        //直接重定向到登录页面
		log.info("------------user not login---------");
        response.sendRedirect(request.getContextPath() + "/me/lyear_pages_login.html");
        return false;

    }

}
package com.ljy.graduate.service;

import com.ljy.graduate.bean.UserVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.exception.GraduateException;

import javax.mail.MessagingException;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
public interface UserService {
    Response<Boolean> register(UserVO userVO) throws GraduateException, MessagingException;

    Response<Boolean> activateUser(String email);

    Response<Boolean> login(UserVO userVO) throws GraduateException;

    Response<Boolean> updatePassword(String oldPassword, String newPassword, String email);

    Response<UserVO> getUserProfile(String email);

    Response<UserVO> updateProfile(String email, String userName);
}

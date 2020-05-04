package com.ljy.graduate.service.impl;

import com.ljy.graduate.bean.UserVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.dao.UserDao;
import com.ljy.graduate.entity.User;
import com.ljy.graduate.exception.GraduateException;
import com.ljy.graduate.service.MailService;
import com.ljy.graduate.service.UserService;
import com.ljy.graduate.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;

import static com.ljy.graduate.common.ResponseMessage.*;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Component("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource(name = "mailService")
    MailService mailService;
    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public Response<Boolean> register(UserVO userVO) throws GraduateException, MessagingException {
        if (userVO == null) {
            throw new GraduateException("参数为空");
        }
        //发送邮件
        User user = userDao.findByEmail(userVO.getEmail());
        if (user != null) {
            return new Response<>(ResponseMessage.USER_EXIST);
        }
        mailService.sendVerifyMail(userVO.getEmail(),
            Constants.UserConstants.REGISTER_MAIL_TITLE, getEmailContent(userVO.getEmail()));

        user = new User();
        user.setStatus(0);
        user.setEmail(userVO.getEmail());
        user.setCreateTime(new Date());
        user.setPassword(userVO.getPassword());
        user.setUserName(userVO.getUserName());
        userDao.save(user);
        return new Response<>(true);
    }

    public Response<Boolean> activateUser(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return new Response<>(false);
        }
        if (user.getStatus() == 1) {
            return new Response<>(true);
        }
        user.setStatus(1);
        userDao.save(user);
        return new Response<>(true);
    }

    @Override
    public Response<Boolean> login(UserVO userVO) throws GraduateException {
        if (userVO == null) {
            throw new GraduateException("参数为空");
        }
        User user = userDao.findByEmail(userVO.getEmail());
        if (user == null) {
            return new Response<>(USER_NOT_EXIST);
        }
        if (user.getStatus() == 0) {
            return new Response<>(USER_NOT_ACTIVE);
        }
        if (!user.getPassword().equals(userVO.getPassword())) {
            return new Response<>(USER_PASSWORD_NOT_TRUE);
        }

        return new Response<>(true);
    }

    @Override
    public Response<Boolean> updatePassword(String oldPassword, String newPassword, String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return new Response<>(USER_NOT_EXIST);
        }
        if (!user.getPassword().equals(oldPassword)) {
            return new Response<>(USER_OLD_PASSWORD_ERROR);
        }
        user.setPassword(newPassword);
        userDao.save(user);
        return new Response<>(true);
    }

    @Override
    public Response<UserVO> getUserProfile(String email) {
        User user = userDao.findByEmail(email);
        UserVO userVO = new UserVO();
        userVO.setEmail(user.getEmail());
        userVO.setUserName(user.getUserName());
        return new Response<>(userVO);
    }

    @Override
    public Response<UserVO> updateProfile(String email, String userName) {
        User user = userDao.findByEmail(email);
        user.setUserName(userName);
        userDao.save(user);
        return getUserProfile(email);
    }

    public String getEmailContent(String email) {
        return
            "亲爱的用户：<br/>感谢您在我站注册了新帐号。<br/><br/><a href='http://localhost:8080/user/activateUser?email=" + email + "'target= _blank'>请点击链接激活您的帐号</a><br/> 如果以上链接无法点击，请将它复制到你的浏览器地址栏中进入访问，该链接24小时内有效。";
    }
}

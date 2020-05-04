package com.ljy.graduate.service;

import javax.mail.MessagingException;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/29
 * Description:
 */
public interface MailService {

    /**
     * 发送邮件
     * @param to
     * @param title
     * @param content
     */
    void sendVerifyMail(String to, String title, String content) throws MessagingException;

}

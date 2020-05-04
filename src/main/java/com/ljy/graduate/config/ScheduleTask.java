package com.ljy.graduate.config;

import com.ljy.graduate.service.EnvironmentDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class ScheduleTask {
    //3.添加定时任务

    @Resource(name = "environmentDetectionService")
    private EnvironmentDetectionService environmentDetectionService;

    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate = 10000)
    private void configureTasks() {
        try {
            environmentDetectionService.changeEnvironmentData();
        } catch (MessagingException e) {
            log.error("发送环境警报邮件失败");
        }
    }
}
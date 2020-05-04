package com.ljy.graduate.config;

import com.ljy.graduate.service.EnvironmentDetectionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {
    //3.添加定时任务

    @Resource(name = "environmentDetectionService")
    private EnvironmentDetectionService environmentDetectionService;

    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate = 10000)
    private void configureTasks() {
        environmentDetectionService.changeEnvironmentData();
    }
}
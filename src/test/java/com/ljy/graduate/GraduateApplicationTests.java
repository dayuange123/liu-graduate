package com.ljy.graduate;

import com.ljy.graduate.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class GraduateApplicationTests {

    @Resource(name = "dataService")
    DataService dataService;
    @Test
    void contextLoads() {
        dataService.getDeviceAlarmData("695510719@qq.com");
    }

}

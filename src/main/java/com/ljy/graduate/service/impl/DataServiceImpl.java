package com.ljy.graduate.service.impl;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.AlarmVO;
import com.ljy.graduate.bean.DataVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Service("dataService")
public class DataServiceImpl implements DataService {
    @Resource(name = "deviceService")
    private DeviceService deviceService;
    @Resource(name = "environmentService")
    private EnvironmentService environmentService;

    @Resource(name = "environmentHistoryService")
    private EnvironmentHistoryService environmentHistoryService;

    @Resource(name = "deviceHistoryService")
    private DeviceHistoryService deviceHistoryService;


    @Override
    public Response<DataVO> getData(String email) {
        DataVO dataVO = new DataVO();
        dataVO.setDeviceCount(deviceService.getDeviceCount(email));
        dataVO.setEnCount(environmentService.getEnvironmentCount(email));

        dataVO.setDeviceAlarm(deviceHistoryService.getAlarmCount(email));
        dataVO.setEnAlarm(environmentHistoryService.getAlarmCount(email));

        return new Response<>(dataVO);
    }

    @Override
    public Response<AlarmDataVO> getDeviceAlarmData(String email) {
        AlarmDataVO alarmVOS= deviceHistoryService.getDeviceAlarmData(email);
        dealData(alarmVOS);
        return new Response<>(alarmVOS);
    }


    private void dealData(AlarmDataVO alarmDataVO){
        List<Integer> oldTimes = alarmDataVO.getTimes();
        LinkedList<Integer> oldCounts =new LinkedList<>(alarmDataVO.getCounts());
        List<Integer> newTimes=new ArrayList<>();
        List<Integer> newCounts=new ArrayList<>();
        for (int i=0;i<24;i++){
            if(oldTimes.contains(i)){
                newTimes.add(i);
                newCounts.add(oldCounts.removeFirst());
            }else {
                newTimes.add(i);
                newCounts.add(0);
            }
        }
        alarmDataVO.setTimes(newTimes);
        alarmDataVO.setCounts(newCounts);
    }
    @Override
    public Response<AlarmDataVO> getEnvironmentAlarmData(String email) {
        AlarmDataVO alarmData = environmentHistoryService.getAlarmData(email);
        dealData(alarmData);
        return new Response<>(alarmData);
    }
}

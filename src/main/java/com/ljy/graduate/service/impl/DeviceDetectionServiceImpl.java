package com.ljy.graduate.service.impl;

import com.alibaba.fastjson.JSON;
import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.DeviceDao;
import com.ljy.graduate.dao.RedisDao;
import com.ljy.graduate.entity.Device;
import com.ljy.graduate.entity.DeviceHistory;
import com.ljy.graduate.service.DeviceDetectionService;
import com.ljy.graduate.service.DeviceHistoryService;
import com.ljy.graduate.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.ljy.graduate.util.Constants.UserConstants.ALARM_MAIL_DEVICE_CONTENT;
import static com.ljy.graduate.util.Constants.UserConstants.ALARM_MAIL_TITLE;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Service("deviceDetectionService")
@Slf4j
public class DeviceDetectionServiceImpl implements DeviceDetectionService {


    @Resource(name = "deviceDao")
    private DeviceDao deviceDao;

    @Resource(name = "redisDao")
    private RedisDao redisDao;
    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @Resource(name = "deviceHistoryService")
    private DeviceHistoryService deviceHistoryService;

    @Resource(name = "mailService")
    private MailService mailService;

    private ConcurrentHashMap<Integer, Integer> alarmMap = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private DeviceDetectionVO buildNewData(Device d) {
        return DeviceDetectionVO.builder().status(0)
            .isError(false).id(d.getId())
            .reportThreshold(d.getReportThreshold())
            .useTime(0L).name(d.getDeviceName()).area(d.getDeviceArea()).build();
    }

    @Override
    public Response<List<DeviceDetectionVO>> getAllDeviceData(String email) throws ParseException, MessagingException {
        List<DeviceDetectionVO> detectionAll = new ArrayList<>();

        List<Device> allDevice = deviceDao.findAllByEmail(email);

        //处理redis中原本数据
        String deviceDataStr = redisDao.getDeviceDataByEmail(email);
        if (deviceDataStr != null) {
            List<DeviceDetectionVO> detectionFromRedis = JSON.parseArray(deviceDataStr, DeviceDetectionVO.class);
            Map<Integer, DeviceDetectionVO> redisDataMap = detectionFromRedis.stream().
                collect(Collectors.toMap(DeviceDetectionVO::getId, d -> d));
            //如果存在 加入到detectionAll，不存在的直接无视
            allDevice.forEach(device -> {
                if (redisDataMap.containsKey(device.getId())) {
                    detectionAll.add(redisDataMap.get(device.getId()));
                } else {
                    detectionAll.add(buildNewData(device));
                }
            });
        } else {
            allDevice.forEach(device -> detectionAll.add(buildNewData(device)));
        }

        //更新数据
        for (DeviceDetectionVO detectionVO : detectionAll) {
            if (detectionVO.getStatus() == 1) {
                long start = format.parse(detectionVO.getStartTime()).getTime();
                detectionVO.setUseTime((System.currentTimeMillis() - start) / 1000 / 60);
                detectionVO.setIsError(detectionVO.getUseTime() > Integer.parseInt(detectionVO.getReportThreshold()));
                //报警处理
                executorService.execute(() -> {
                    try {
                        doAlarm(detectionVO, email);
                    } catch (MessagingException e) {
                        log.error("发送邮件错误");
                    }
                });

            }
        }
        redisDao.setDeviceDataByEmail(email, JSON.toJSON(detectionAll).toString());
        log.info("get DeviceDetectionVO={}", detectionAll.toString());
        return new Response<>(detectionAll);
    }

    @Override
    public Response<Boolean> operateDevice(String email, Integer deviceId, Integer status) throws ParseException, MessagingException {

        String deviceDataStr = redisDao.getDeviceDataByEmail(email);

        List<DeviceDetectionVO> oldDetectionVOS = JSON.parseArray(deviceDataStr, DeviceDetectionVO.class);
        for (DeviceDetectionVO detectionVO : oldDetectionVOS) {
            if (detectionVO.getId().equals(deviceId)) {
                if (status == 0) {
                    log.info("close device,name={}", detectionVO.getName());
                    //关闭
                    if (detectionVO.getStatus() == 1) {
                        detectionVO.setStatus(0);
                        detectionVO.setEndTime(format.format(System.currentTimeMillis()));
                        long start = format.parse(detectionVO.getStartTime()).getTime();
                        long end = format.parse(detectionVO.getEndTime()).getTime();
                        detectionVO.setUseTime((end - start) / 1000 / 60);
                        detectionVO.setIsError(detectionVO.getUseTime() > Integer.parseInt(detectionVO.getReportThreshold()));
                    }
                    //报警处理
                    executorService.execute(() -> {
                        try {
                            doAlarm(detectionVO, email);
                        } catch (MessagingException e) {
                            log.error("发送邮件错误");
                        }
                    });

                    deviceHistoryService.addHistoryData(buildHistory(detectionVO, email));
                } else {
                    //打开
                    log.info("open device,name={}", detectionVO.getName());
                    if (detectionVO.getStatus() == 0) {
                        detectionVO.setStatus(1);
                        detectionVO.setStartTime(format.format(System.currentTimeMillis()));
                        detectionVO.setUseTime(0L);
                        detectionVO.setIsError(false);
                    }
                }
            }

        }
        redisDao.setDeviceDataByEmail(email, JSON.toJSON(oldDetectionVOS).toString());
        return new Response<>(true);
    }

    private void doAlarm(DeviceDetectionVO detectionVO, String email) throws MessagingException {
        if (detectionVO.getIsError()) {
            Integer val = alarmMap.getOrDefault(detectionVO.getId(), 0);
            if (val % 10 == 0) {
                mailService.sendAlarmMail(email, ALARM_MAIL_TITLE, buildAlarmContent(detectionVO));
            }
            alarmMap.put(detectionVO.getId(), val + 1);
            log.info("alarm val={}", val);
        } else {
            alarmMap.remove(detectionVO.getId());
        }

    }

    private String buildAlarmContent(DeviceDetectionVO detectionVO) {
        return ALARM_MAIL_DEVICE_CONTENT + "\n设备名:" + detectionVO.getName() + "\n" +
            "设备区域:" + detectionVO.getArea() + "\n" +
            "设备使用时间:" + detectionVO.getUseTime() + "\n" +
            "设备报警阈值:" + detectionVO.getReportThreshold() + "\n";
    }

    private DeviceHistory buildHistory(DeviceDetectionVO detectionVO, String email) {

        return DeviceHistory.builder()
            .area(detectionVO.getArea())
            .createTime(new Date())
            .email(email)
            .endTime(detectionVO.getEndTime())
            .startTime(detectionVO.getStartTime())
            .name(detectionVO.getName())
            .status(detectionVO.getIsError() ? 0 : 1)
            .useTime(detectionVO.getUseTime().intValue()).build();
    }


}

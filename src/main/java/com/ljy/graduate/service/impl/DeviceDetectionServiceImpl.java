package com.ljy.graduate.service.impl;

import com.alibaba.fastjson.JSON;
import com.ljy.graduate.bean.DeviceDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.DeviceDao;
import com.ljy.graduate.dao.RedisDao;
import com.ljy.graduate.entity.Device;
import com.ljy.graduate.service.DeviceDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    private DeviceDetectionVO buildNewData(Device d) {
        return DeviceDetectionVO.builder().status(0)
            .isError(false).id(d.getId())
            .reportThreshold(d.getReportThreshold())
            .useTime(0L).nameAndArea(d.getDeviceName() + "(" + d.getDeviceArea() + ")").build();
    }

    @Override
    public Response<List<DeviceDetectionVO>> getAllDeviceData(String email) throws ParseException {
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
                //过滤存储新的数据
                if (detectionVO.getIsError()) {
                    //TODO 发送邮件告诉
                }
            }
        }
        redisDao.setDeviceDataByEmail(email, JSON.toJSON(detectionAll).toString());
        log.info("get DeviceDetectionVO={}", detectionAll.toString());
        return new Response<>(detectionAll);
    }

    @Override
    public Response<Boolean> operateDevice(String email, Integer deviceId, Integer status) throws ParseException {

        String deviceDataStr = redisDao.getDeviceDataByEmail(email);

        List<DeviceDetectionVO> oldDetectionVOS = JSON.parseArray(deviceDataStr, DeviceDetectionVO.class);
        for (DeviceDetectionVO detectionVO : oldDetectionVOS) {
            if (detectionVO.getId().equals(deviceId)) {
                if (status == 0) {
                    log.info("close device,name={}", detectionVO.getNameAndArea());
                    //关闭
                    if (detectionVO.getStatus() == 1) {
                        detectionVO.setStatus(0);
                        detectionVO.setEndTime(format.format(System.currentTimeMillis()));
                        long start = format.parse(detectionVO.getStartTime()).getTime();
                        long end = format.parse(detectionVO.getEndTime()).getTime();
                        detectionVO.setUseTime((end - start) / 1000 / 60);
                        detectionVO.setIsError(detectionVO.getUseTime() > Integer.parseInt(detectionVO.getReportThreshold()));
                    }
                    if (detectionVO.getIsError()) {
                        //TODO 发送邮件告诉
                    }
                    //TODO 上报历史记录
                } else {
                    //打开
                    log.info("open device,name={}", detectionVO.getNameAndArea());
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
}

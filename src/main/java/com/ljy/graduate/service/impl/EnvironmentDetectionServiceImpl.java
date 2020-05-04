package com.ljy.graduate.service.impl;

import com.alibaba.fastjson.JSON;
import com.ljy.graduate.bean.EnvironmentDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.EnvironmentDao;
import com.ljy.graduate.dao.RedisDao;
import com.ljy.graduate.dao.UserDao;
import com.ljy.graduate.entity.Environment;
import com.ljy.graduate.entity.EnvironmentHistory;
import com.ljy.graduate.entity.User;
import com.ljy.graduate.service.EnvironmentDetectionService;
import com.ljy.graduate.service.EnvironmentHistoryService;
import com.ljy.graduate.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.ljy.graduate.util.Constants.UserConstants.ALARM_MAIL_ENVIRONMENT_CONTENT;
import static com.ljy.graduate.util.Constants.UserConstants.ALARM_MAIL_TITLE;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Service("environmentDetectionService")
@Slf4j
public class EnvironmentDetectionServiceImpl implements EnvironmentDetectionService {

    @Resource(name = "environmentDao")
    private EnvironmentDao environmentDao;


    @Resource(name = "redisDao")
    private RedisDao redisDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    @Resource(name = "mailService")
    private MailService mailService;

    private ConcurrentHashMap<Integer, Integer> alarmMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> reportMap = new ConcurrentHashMap<>();

    @Resource(name = "environmentHistoryService")
    private EnvironmentHistoryService environmentHistoryService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 初始化数据值
     *
     * @param environment
     * @return
     */
    private EnvironmentDetectionVO buildNewData(Environment environment) {
        return EnvironmentDetectionVO.builder().area(environment.getArea())
            .current(0d).isError(false).name(environment.getName())
            .normal(environment.getNormal())
            .updateTime(format.format(System.currentTimeMillis()))
            .reportThreshold(environment.getReportThreshold())
            .id(environment.getId())
            .build();

    }

    @Override
    public Response<List<EnvironmentDetectionVO>> getAllData(String email) {

        String environmentDataStr = redisDao.getEnvironmentDataByEmail(email);
        if (environmentDataStr == null) {
            return new Response<>(new ArrayList<>());
        }
        List<EnvironmentDetectionVO> detectionFromRedis = JSON.parseArray(environmentDataStr, EnvironmentDetectionVO.class);

        return new Response<>(detectionFromRedis);
    }

    public void changeEnvironmentData() throws MessagingException {
        List<String> emailList = userDao.findAll().stream().map(User::getEmail).collect(Collectors.toList());
        for (String email : emailList) {
            List<Environment> environments = environmentDao.findAllByEmail(email);
            if (environments == null || environments.size() == 0) {
                continue;
            }
            changeCore(environments, email);
        }
    }

    private void changeCore(List<Environment> environments, String email) throws MessagingException {
        List<EnvironmentDetectionVO> newData = new ArrayList<>();
        String oldDataStr = redisDao.getEnvironmentDataByEmail(email);
        if (oldDataStr == null || oldDataStr.equals("")) {
            environments.forEach(e -> newData.add(buildNewData(e)));
        } else {
            List<EnvironmentDetectionVO> oldData = JSON.parseArray(oldDataStr, EnvironmentDetectionVO.class);
            Map<Integer, EnvironmentDetectionVO> redisDataMap = oldData.stream().
                collect(Collectors.toMap(EnvironmentDetectionVO::getId, d -> d));
            //如果存在 加入到detectionAll，不存在的直接无视
            environments.forEach(environment -> {
                if (redisDataMap.containsKey(environment.getId())) {
                    newData.add(redisDataMap.get(environment.getId()));
                } else {
                    newData.add(buildNewData(environment));
                }
            });
        }
        //动态调整该数据值
        for (EnvironmentDetectionVO newDatum : newData) {
            Double normal = newDatum.getNormal();
            Double reportThreshold = newDatum.getReportThreshold();
            Double current = newDatum.getCurrent();
            Random random = new Random();
            if (random.nextInt(10) == 5) {
                //超过阈值
                newDatum.setCurrent((random.nextInt() % normal) / 10 + reportThreshold);
            } else {
                if (current == null || current == 0d) {
                    newDatum.setCurrent(normal);
                } else {
                    if (current < normal) {
                        newDatum.setCurrent(current + current / (random.nextInt(10) + 10.0));
                    } else {
                        newDatum.setCurrent(current - current / (random.nextInt(10) + 10.0));
                    }
                }
            }
            newDatum.setIsError(newDatum.getCurrent() > reportThreshold);
            newDatum.setUpdateTime(format.format(new Date().getTime()));
            doAlarm(newDatum, email);
            doReport(newDatum, email);

        }
        updateEnvironmentData(email, newData);
    }

    private void doReport(EnvironmentDetectionVO newDatum, String email) {
        Integer val = reportMap.getOrDefault(newDatum.getId(), 0);
        if (val % 10 == 0) {
            environmentHistoryService.addData(buildHistory(newDatum, email));
        }
        reportMap.put(newDatum.getId(), val + 1);
    }


    private EnvironmentHistory buildHistory(EnvironmentDetectionVO environmentDetectionVO, String email) {

        return EnvironmentHistory.builder()
            .area(environmentDetectionVO.getArea())
            .createTime(new Date())
            .email(email)
            .updateTime(environmentDetectionVO.getUpdateTime())
            .name(environmentDetectionVO.getName())
            .current(environmentDetectionVO.getCurrent())
            .status(environmentDetectionVO.getIsError() ? 0 : 1).build();
    }

    /**
     * 更新数据
     */
    private void updateEnvironmentData(String email, List<EnvironmentDetectionVO> environmentVOS) {
        String string = JSON.toJSON(environmentVOS).toString();
        log.info("update environment data={}", string);
        redisDao.setEnvironmentDataByEmail(email, string);
    }


    private void doAlarm(EnvironmentDetectionVO detectionVO, String email) throws MessagingException {
        if (detectionVO.getIsError()) {
            Integer val = alarmMap.getOrDefault(detectionVO.getId(), 0);
            if (val % 20 == 0) {
                mailService.sendAlarmMail(email, ALARM_MAIL_TITLE, buildAlarmContent(detectionVO));
            }
            alarmMap.put(detectionVO.getId(), val + 1);
            log.info("alarm val={}", val);
        } else {
            alarmMap.remove(detectionVO.getId());
        }

    }

    private String buildAlarmContent(EnvironmentDetectionVO detectionVO) {
        return ALARM_MAIL_ENVIRONMENT_CONTENT + "\n设备名:" + detectionVO.getName() + "\n" +
            "设备区域:" + detectionVO.getArea() + "\n" +
            "设备当前值:" + detectionVO.getCurrent() + "\n" +
            "设备报警阈值:" + detectionVO.getReportThreshold() + "\n";
    }
}

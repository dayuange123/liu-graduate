package com.ljy.graduate.service.impl;

import com.alibaba.fastjson.JSON;
import com.ljy.graduate.bean.EnvironmentDetectionVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.EnvironmentDao;
import com.ljy.graduate.dao.RedisDao;
import com.ljy.graduate.dao.UserDao;
import com.ljy.graduate.entity.Environment;
import com.ljy.graduate.entity.User;
import com.ljy.graduate.service.EnvironmentDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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


    public void changeEnvironmentData() {
        List<String> emailList = userDao.findAll().stream().map(User::getEmail).collect(Collectors.toList());
        for (String email : emailList) {
            List<Environment> environments = environmentDao.findAllByEmail(email);
            if (environments == null || environments.size() == 0) {
                continue;
            }
            changeCore(environments, email);
        }
    }

    private void changeCore(List<Environment> environments, String email) {
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
                newDatum.setCurrent((random.nextInt() % normal)/10 + reportThreshold);
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
            newDatum.setUpdateTime(format.format(System.currentTimeMillis()));
            if(newDatum.getIsError()){
                //TODO 发送短信告知
            }
            //TODO 上报统计历史
        }
        updateEnvironmentData(email, newData);
    }

    /**
     * 更新数据
     */
    private void updateEnvironmentData(String email, List<EnvironmentDetectionVO> environmentVOS) {
        String string = JSON.toJSON(environmentVOS).toString();
        log.info("update environment data={}", string);
        redisDao.setEnvironmentDataByEmail(email, string);
    }
}

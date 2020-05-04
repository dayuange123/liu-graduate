package com.ljy.graduate.dao.impl;


import com.ljy.graduate.dao.RedisDao;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/29
 * Description:
 */
@Component("redisDao")
public class RedisDaoImpl implements RedisDao {

    private static final String PREFIX = "ji:";
    @Resource(name = "stringTemplate")
    private RedisTemplate<String, String> stringTemplate;

    @Override
    public String getDeviceDataByEmail(String email) {
        return stringTemplate.opsForValue().get(PREFIX + email + ":device");

    }

    @Override
    public void setDeviceDataByEmail(String email, String newData) {
        stringTemplate.opsForValue().set(PREFIX + email + ":device", newData);

    }

    @Override
    public void setEnvironmentDataByEmail(String email, String newData) {
        stringTemplate.opsForValue().set(PREFIX + email + ":environment", newData);

    }

    @Override
    public String getEnvironmentDataByEmail(String email) {
        return stringTemplate.opsForValue().get(PREFIX + email + ":environment");

    }
}

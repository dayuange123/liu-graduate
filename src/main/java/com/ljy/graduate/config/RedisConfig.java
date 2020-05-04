package com.ljy.graduate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Value("${spring.redis.host}")
    protected String host;
    @Value("${spring.redis.port}")
    protected int port;
    @Value("${spring.redis.jedis.pool.min-idle}")
    protected int minIdle = 5;
    @Value("${spring.redis.jedis.pool.max-idle}")
    protected int maxIdle = 25;

    protected int maxTotal = 25;
    @Value("${spring.redis.jedis.pool.max-wait}")
    protected int waitMillis = 1000;

    @Bean(name = "stringTemplate")
    protected RedisTemplate<String, String> stringTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }

    private RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory fac = new JedisConnectionFactory();
        fac.setHostName(host);
        fac.setPort(port);
        fac.setUsePool(true);
        fac.setPoolConfig(jedisPoolConfig());
        return fac;
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(waitMillis);
        jedisPoolConfig.setMaxTotal(20);
        return jedisPoolConfig;
    }

}
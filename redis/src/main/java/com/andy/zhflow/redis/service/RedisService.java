package com.andy.zhflow.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据key读取数据
     */
    public String get(final String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            Object o= redisTemplate.opsForValue().get(key);
            if(o instanceof String){
                return (String) o;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入数据
     */
    public boolean set(final String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            log.info("存入redis成功，key：{}，value：{}", key, value);
            return true;
        } catch (Exception e) {
            log.error("存入redis失败，key：{}，value：{}", key, value);
            e.printStackTrace();
        }
        return false;
    }

    public boolean set(final String key, Object value,long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value,timeout,unit);
            log.info("存入redis成功，key：{}，value：{}", key, value);
            return true;
        } catch (Exception e) {
            log.error("存入redis失败，key：{}，value：{}", key, value);
            e.printStackTrace();
        }
        return false;
    }
}
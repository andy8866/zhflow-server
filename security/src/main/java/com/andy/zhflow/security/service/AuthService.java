package com.andy.zhflow.security.service;

import com.andy.zhflow.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuthService {

    @Autowired
    private RedisService redisService;

    public String createToken(String userId){
        String token= UUID.randomUUID().toString();
        redisService.set(token,userId,3600, TimeUnit.SECONDS);
        return token;
    }
}

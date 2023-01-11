package com.andy.zhflow.auth;

import com.andy.zhflow.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuthService {

    @Autowired
    private RedisUtils redisUtil;

    public String createToken(User user){
        String token= UUID.randomUUID().toString();
        redisUtil.set(token,user.getId(),3600, TimeUnit.SECONDS);
        return token;
    }
}

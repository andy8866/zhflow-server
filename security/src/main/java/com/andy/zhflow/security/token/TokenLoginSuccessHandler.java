package com.andy.zhflow.security.token;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.response.ResponseUtil;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.SecurityUser;
import com.andy.zhflow.security.config.SecurityConfig;
import com.andy.zhflow.security.config.SecurityStarterAutoConfigure;
import com.andy.zhflow.utils.UUIDUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class TokenLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedisService redisService;

    public TokenLoginSuccessHandler(RedisService redisService){
        this.redisService=redisService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println("用户:" + securityUser.toString());

        // 创建令牌
        String token = NanoIdUtils.randomNanoId();
        String json= JSON.toJSONString(securityUser);

        redisService.set(token,json, SecurityStarterAutoConfigure.EXPIRATION, TimeUnit.SECONDS);

        response.setHeader("token", token);

        ResponseUtil.writeObject(response, ResultResponse.success(token));
    }
}

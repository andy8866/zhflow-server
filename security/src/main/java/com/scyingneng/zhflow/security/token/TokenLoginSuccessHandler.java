package com.scyingneng.zhflow.security.token;

import com.alibaba.fastjson.JSON;
import com.scyingneng.zhflow.config.BaseConfig;
import com.scyingneng.zhflow.redis.service.RedisService;
import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.security.SecurityUser;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String token ="USER"+NanoIdUtils.randomNanoId();
        String json= JSON.toJSONString(securityUser);

        redisService.set(token,json, BaseConfig.EXPIRATION, TimeUnit.SECONDS);

        ResponseUtil.writeObject(response, ResultResponse.success(token));
    }
}

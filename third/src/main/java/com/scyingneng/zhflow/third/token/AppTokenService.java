package com.scyingneng.zhflow.third.token;

import com.alibaba.fastjson.JSON;
import com.scyingneng.zhflow.config.BaseConfig;
import com.scyingneng.zhflow.redis.service.RedisService;
import com.scyingneng.zhflow.service.thirdApp.IAppTokenService;
import com.scyingneng.zhflow.vo.AppTokenVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AppTokenService implements IAppTokenService {
    @Autowired
    private RedisService redisService;


    public String getAppToken(String appId, String userId) throws Exception {

        // 创建令牌
        String token ="APP"+NanoIdUtils.randomNanoId();

        AppTokenVO appTokenVO=new AppTokenVO(appId, userId);
        String json= JSON.toJSONString(appTokenVO);

        redisService.set(token,json, BaseConfig.EXPIRATION, TimeUnit.SECONDS);

        return token;
    }
}

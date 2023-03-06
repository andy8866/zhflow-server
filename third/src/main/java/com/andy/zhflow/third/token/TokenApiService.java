package com.andy.zhflow.third.token;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.config.BaseConfig;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.third.app.App;
import com.andy.zhflow.third.utils.BaseSignVO;
import com.andy.zhflow.third.utils.SignUtil;
import com.andy.zhflow.vo.AppTokenVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenApiService {
    @Autowired
    private RedisService redisService;


    public String getToken(BaseSignVO signVO) throws Exception {
        SignUtil.verify(signVO.toTreeMap(),App.getAppKey(signVO.getAppId()));

        // 创建令牌
        String token ="APP"+NanoIdUtils.randomNanoId();

        AppTokenVO appTokenVO=new AppTokenVO(signVO.getAppId(), signVO.getUserId());
        String json= JSON.toJSONString(appTokenVO);

        redisService.set(token,json, BaseConfig.EXPIRATION, TimeUnit.SECONDS);

        return token;
    }
}

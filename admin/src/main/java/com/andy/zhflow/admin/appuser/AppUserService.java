package com.andy.zhflow.admin.appuser;

import com.andy.zhflow.app.App;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserService {

    @Autowired
    private RedisService redisService;

    public App getSelectApp() {
        String appId = (String) redisService.get("selectApp." + UserUtil.getUserId());
        if(StringUtils.isEmpty(appId)){
            return null;
        }

        return App.getById(appId);
    }

    public void setSelectApp(String appId) {
        redisService.set("selectApp." + UserUtil.getUserId(),appId);
    }
}

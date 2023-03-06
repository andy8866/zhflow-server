package com.andy.zhflow.third.api;

import com.andy.zhflow.utils.RequestUtil;
import org.springframework.stereotype.Component;

@Component
public class ApiService {

    public String getUserId(){
        return RequestUtil.getParam("userId");
    }

    public String getAppId(){
        return RequestUtil.getParam("appId");
    }

}

package com.andy.zhflow.third.call;

import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSON;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.third.app.App;
import com.andy.zhflow.third.appConfig.AppConfig;
import com.andy.zhflow.third.appConfig.AppConfigHttpRequestVO;
import com.andy.zhflow.third.utils.OkHttpUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CallService {

    public <T> T callApi(String appId, String code, Map<String, String> params) throws Exception {
        App app=App.getApp(appId);

        AppConfigHttpRequestVO configVO= (AppConfigHttpRequestVO) AppConfig.getVO(appId,code);

        String resultStr;
        if(configVO.method.equals("get")){
            resultStr = OkHttpUtil.get(app.getRootUrl(), params, app.getSecretKey());
        }else{
            resultStr = OkHttpUtil.post(app.getRootUrl(), params, app.getSecretKey());
        }

        ResultResponse<T> resultResponse = JSON.parseObject(resultStr, new TypeReference<ResultResponse<T>>() {});
        if(!resultResponse.isSuccess()) throw new Exception(resultResponse.getMsg());

        return resultResponse.getData();
    }
}

package com.andy.zhflow.third.call;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.service.third.IThirdCallService;
import com.andy.zhflow.third.app.App;
import com.andy.zhflow.third.appConfig.AppConfig;
import com.andy.zhflow.third.okHttp.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.TypeReference;

@Component
@Slf4j
public class ThirdCallService implements IThirdCallService {

    public <T> T callApi(String appId, String code, Map<String, Object> params,Class<T> clazz) throws Exception {
        App app=App.getApp(appId);

        String urlPath="";

        if(code.equals(ThirdCallCodeConstant.GET_APP_TOKEN)){
            urlPath="/api/third/token/getToken";
        }else{
            AppConfig configVO= AppConfig.getConfig(appId,code);
            urlPath=configVO.getHttpUrlPath();
        }

        Map<String, Object> signMap=new HashMap<>();
        signMap.put("appId",appId);

        String resultStr;
        String url=app.getRootUrl()+urlPath;

//        resultStr = OkHttpUtil.get(url, params,signMap, app.getSecretKey());
        resultStr = OkHttpUtil.postJsonParams(url,signMap, app.getSecretKey(),params);
        if(StringUtils.isEmpty(resultStr)) throw new Exception("第三方返回数据为空或第三方调用接口错误");

        log.info(String.valueOf(clazz));
        ResultResponse<T> resultResponse = JSON.parseObject(resultStr, new TypeReference<ResultResponse<T>>() {});
        if(!resultResponse.isSuccess()) throw new Exception(resultResponse.getMsg());

        return resultResponse.getData();
    }

    public String getAppToken(String appId, String userId) throws Exception {
        Map<String,Object> params=new HashMap<>();
        params.put("appId",appId);
        params.put("userId",userId);
        return callApi(appId, ThirdCallCodeConstant.GET_APP_TOKEN, params,String.class);
    }

    public String getSuperiorUserId(String appId,String userId) throws Exception {
        Map<String,Object> params=new HashMap<>();
        params.put("appId",appId);
        params.put("userId",userId);
        return callApi(appId, ThirdCallCodeConstant.GET_SUPERIOR_USER_ID, params,String.class);
    }

    public String getUserNameById(String appId,String userId) throws Exception {
        Map<String,Object> params=new HashMap<>();
        params.put("appId",appId);
        params.put("userId",userId);
        return callApi(appId,ThirdCallCodeConstant.GET_USER_NAME_BY_ID, params,String.class);
    }
}

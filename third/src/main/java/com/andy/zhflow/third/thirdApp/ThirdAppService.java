package com.andy.zhflow.third.thirdApp;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.service.thirdApp.IThirdAppService;
import com.andy.zhflow.third.app.App;
import com.andy.zhflow.third.appConfig.AppConfig;
import com.andy.zhflow.third.okHttp.OkHttpUtil;
import com.andy.zhflow.vo.SelectOutVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.TypeReference;

import javax.lang.model.element.TypeElement;

@Component
@Slf4j
public class ThirdAppService implements IThirdAppService {

    @Autowired
    private IAuthService authService;

    public <T> T callApi(String appId, String code, Map<String, Object> params) throws Exception {
        return callApi(appId,code,params,null);
    }
    public <T> T callApi(String appId, String code, Map<String, Object> params, TypeReference typeReference) throws Exception {
        App app=App.getApp(appId);

        AppConfig configVO= AppConfig.getConfig(appId,code);
        String urlPath=configVO.getHttpUrlPath();

        Map<String, Object> signMap=new HashMap<>();
        signMap.put("appId",appId);

        String resultStr;
        String url=app.getRootUrl()+urlPath;

//        resultStr = OkHttpUtil.get(url, params,signMap, app.getSecretKey());
        resultStr = OkHttpUtil.postJsonParams(url,signMap, app.getSecretKey(),params);
        if(StringUtils.isEmpty(resultStr)) throw new Exception("第三方返回数据为空或第三方调用接口错误");

        ResultResponse<T> resultResponse =JSON.parseObject(resultStr, new TypeReference<ResultResponse<T>>() {});
        if(!resultResponse.isSuccess()) throw new Exception(resultResponse.getMsg());

        if(resultResponse.getData()!=null && typeReference!=null){
            String s=JSON.toJSONString(resultResponse.getData());
            resultResponse.setData(JSON.parseObject(s, typeReference));
        }

        return resultResponse.getData();
    }


    public String getSuperiorUserId(String appId,String userId) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);
        params.put("userId", userId);
        return callApi(appId,"getSuperiorUserId", params);
    }

    public String getUserNameById(String appId,String userId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("userId", userId);
        return callApi(appId,"getUserNameById",params);
    }

    public List<SelectOutVO> getDeptListToSelect(String appId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        return callApi(appId,"getDeptListToSelect",params, new TypeReference<List<SelectOutVO>>() {});
    }

    public List<SelectOutVO> getRoleListToSelect(String appId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", authService.getAppId());
        return callApi(appId,"getRoleListToSelect", params,new TypeReference<List<SelectOutVO>>(){});
    }

    public List<SelectOutVO> getUserListToSelect(String appId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);

        return callApi(appId,"getUserListToSelect", params,new TypeReference<List<SelectOutVO>>() {});
    }

    public Map<String,String> getDictValueMap(String appId, String type) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("type", type);
        return callApi(appId,"getDictValueMap", params,new TypeReference<Map<String,String>>() {});
    }
}

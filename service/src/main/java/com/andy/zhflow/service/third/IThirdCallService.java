package com.andy.zhflow.service.third;

import java.util.Map;

public interface IThirdCallService {
    <T> T callApi(String appId, String code, Map<String, Object> params,Class<T> clazz) throws Exception;

    String getAppToken(String thirdAppId, String userId) throws Exception;

    String getSuperiorUserId(String appId,String userId) throws Exception;

    String getUserNameById(String appId,String userId) throws Exception;
}

package com.scyingneng.zhflow.service.thirdApp;

public interface IAppTokenService {

    String getAppToken(String appId, String userId) throws Exception;
}

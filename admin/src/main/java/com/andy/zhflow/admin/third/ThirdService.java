package com.andy.zhflow.admin.third;

import cn.hutool.crypto.asymmetric.Sign;
import com.andy.zhflow.config.BaseConfig;
import com.andy.zhflow.security.utils.AuthUtil;
import com.andy.zhflow.third.token.TokenApiService;
import com.andy.zhflow.third.utils.BaseSignVO;
import com.andy.zhflow.third.utils.SignUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ThirdService  {
    @Autowired
    private BaseConfig baseConfig;


    @Autowired
    private TokenApiService tokenApiService;

    public String getAppToken() throws Exception {
        BaseSignVO signVO=new BaseSignVO();
        signVO.setAppId(baseConfig.getFlowAppId());
        signVO.setUserId(AuthUtil.getUserId());
        signVO.setTimeStamp(String.valueOf(new Date().getTime()));
        signVO.setRandStr(NanoIdUtils.randomNanoId());
        signVO.setSign(SignUtil.sign( signVO.toTreeMap(),baseConfig.getFlowKey()));

        return tokenApiService.getToken(signVO);
    }
}

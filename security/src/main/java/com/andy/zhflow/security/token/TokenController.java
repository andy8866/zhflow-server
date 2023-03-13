package com.andy.zhflow.security.token;

import com.andy.zhflow.config.BaseConfig;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.service.third.IThirdCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/security/token")
public class TokenController {

    @Autowired
    private BaseConfig baseConfig;

    @Autowired
    private IThirdCallService thirdCallService;

    @Autowired
    private IAuthService authService;

    @GetMapping(value="/validateToken")
    public ResultResponse<Void> validateToken() {
        return ResultResponse.success(null);
    }

    @GetMapping(value="/getAppToken")
    public ResultResponse<String> getAppToken() throws Exception {
        String token= thirdCallService.getAppToken(baseConfig.getThirdAppId(), authService.getUserId());
        return ResultResponse.success(token);
    }
}

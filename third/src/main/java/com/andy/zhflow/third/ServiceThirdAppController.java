package com.andy.zhflow.third;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.third.token.GetTokenInputVO;
import com.andy.zhflow.third.token.AppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给三方的接口,签名验证
 */
@RestController()
@RequestMapping(value = "/api/serviceThirdApp")
public class ServiceThirdAppController {

    @Autowired
    private AppTokenService tokenApiService;

    @PostMapping(value="/getToken")
    public ResultResponse<String> getToken(@RequestBody() GetTokenInputVO inputVO) throws Exception {

        String token=tokenApiService.getAppToken(inputVO.getAppId(),inputVO.getUserId());
        return ResultResponse.success(token);
    }
}

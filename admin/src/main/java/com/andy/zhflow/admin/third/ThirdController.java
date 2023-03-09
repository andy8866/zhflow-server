package com.andy.zhflow.admin.third;

import com.andy.zhflow.config.BaseConfig;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.AuthUtil;
import com.andy.zhflow.service.third.IThirdCallService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping(value = "/api/admin/third")
public class ThirdController {

    @Autowired
    private IThirdCallService thirdCallService;

    @Autowired
    private BaseConfig baseConfig;

    @GetMapping(value="/getAppToken")
    public ResultResponse<String> getAppToken() throws Exception {
        String token= thirdCallService.getAppToken(baseConfig.getThirdAppId(),AuthUtil.getUserId());
        return ResultResponse.success(token);
    }

    @PostMapping(value="/call")
    public ResultResponse<Object> call(@RequestBody() Map<String,Object> params) throws Exception {

        String appId= AuthUtil.getAppId();
        String code= (String) params.get("code");
        if(StringUtils.isEmpty(code)) return ResultResponse.fail("缺少code");

        Object result = thirdCallService.callApi(appId, code, params,Object.class);
        return ResultResponse.success(result);
    }
}

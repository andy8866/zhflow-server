package com.scyingneng.zhflow.admin;

import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.service.security.IAuthService;
import com.scyingneng.zhflow.service.thirdApp.IThirdAppService;
import com.scyingneng.zhflow.third.app.App;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IThirdAppService thirdAppService;

    @GetMapping(value="/getCurrentInfo")
    public ResultResponse<AuthCurrentInfoVO> getCurrentInfo() throws Exception {
        AuthCurrentInfoVO infoVO=new AuthCurrentInfoVO();
        if(StringUtils.isNotEmpty(authService.getAppId())){
            infoVO.setAppId(authService.getAppId());
            infoVO.setAppName(App.getName(authService.getAppId()));

            if(StringUtils.isNotEmpty(authService.getUserId())){
                infoVO.setUserId(authService.getUserId());
                infoVO.setUserName(thirdAppService.getUserNameById(authService.getAppId(),authService.getUserId()));
            }

        }
        return ResultResponse.success(infoVO);
    }
}

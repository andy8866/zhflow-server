package com.andy.zhflow.third.token;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/third/token")
public class TokenApiController {

    @Autowired
    private TokenApiService tokenApiService;

    @GetMapping(value="/getToken")
    public ResultResponse<String> getToken(@RequestParam("appId") String appId,
                                           @RequestParam("userId") String userId) throws Exception {
        String token=tokenApiService.getToken(appId,userId);
        return ResultResponse.success(token);
    }
}

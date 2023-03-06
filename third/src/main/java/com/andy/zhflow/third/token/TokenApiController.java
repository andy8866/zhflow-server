package com.andy.zhflow.third.token;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.third.utils.BaseSignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/third/token")
public class TokenApiController {

    @Autowired
    private TokenApiService tokenApiService;

    @PostMapping(value="/getToken")
    public ResultResponse<String> getToken(@RequestBody BaseSignVO signVO) throws Exception {
        String token=tokenApiService.getToken(signVO);
        return ResultResponse.success(token);
    }
}

package com.andy.zhflow.security.token;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/security/token")
public class TokenController {

    @GetMapping(value="/validateToken")
    public ResultResponse<Void> validateToken() {
        return ResultResponse.success(null);
    }

}

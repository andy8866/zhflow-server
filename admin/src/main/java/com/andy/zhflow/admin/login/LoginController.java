package com.andy.zhflow.admin.login;

import com.andy.zhflow.exception.ErrMsgException;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping(value="/login")
    public ResultResponse<String> login(@RequestParam("userName") String userName,
                                        @RequestParam("password") String password
    ) throws ErrMsgException {
        String token=loginService.login(userName,password);
        return new ResultResponse<>(0,"",token);
    }
}

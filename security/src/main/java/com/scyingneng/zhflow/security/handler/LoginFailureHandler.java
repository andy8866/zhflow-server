package com.scyingneng.zhflow.security.handler;

import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String msg="";
        if (exception instanceof LockedException){
            msg="账户被锁定";
        }else if (exception instanceof CredentialsExpiredException){
            msg="密码过期";
        }else if (exception instanceof AccountExpiredException){
            msg="账户过期";
        }else if (exception instanceof DisabledException){
            msg="账户被禁用";
        }else if (exception instanceof BadCredentialsException){
            msg="用户名或者密码输入错误";
        }

        ResponseUtil.writeObject(response, ResultResponse.fail(1,msg,exception.getMessage()));
    }
}

package com.scyingneng.zhflow.security.handler;

import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseUtil.writeObject(response, ResultResponse.fail(2,"权限验证未通过",null));
    }
}

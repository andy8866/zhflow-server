package com.andy.zhflow.security.handler;

import com.andy.zhflow.base.response.ResultResponse;
import com.andy.zhflow.security.jwt.JwtTokenUtils;
import com.andy.zhflow.security.jwt.JwtUser;
import com.andy.zhflow.security.response.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 获取Jwt用户信息
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        System.out.println("用户:" + jwtUser.toString()+"密码:"+jwtUser.getPassword());

        // 记住登录信息
//        boolean isRemember = rememberMe.get() == 1;
        boolean isRemember =false;

        // 获取授权信息
        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        // 创建令牌
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role, isRemember);
//        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);

        ResponseUtil.writeObject(response,ResultResponse.success(token));
    }
}

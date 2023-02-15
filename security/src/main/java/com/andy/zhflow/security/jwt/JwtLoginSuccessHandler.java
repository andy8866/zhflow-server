package com.andy.zhflow.security.jwt;

import com.andy.zhflow.response.ResponseUtil;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        // 获取Jwt用户信息
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println("用户:" + securityUser.toString()+"密码:"+ securityUser.getPassword());

        // 获取授权信息
        String role = "";
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        // 创建令牌
        String token = JwtTokenUtils.createToken(securityUser.getId(), securityUser.getUsername(), role, false);
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);

        ResponseUtil.writeObject(response, ResultResponse.success(token));
    }
}

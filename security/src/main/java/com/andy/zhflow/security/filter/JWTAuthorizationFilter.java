package com.andy.zhflow.security.filter;

import com.andy.zhflow.security.jwt.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // 获取请求头信息
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        // 获取令牌
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        // 获取用户信息
        String username = JwtTokenUtils.getUsername(token);
        // 获取角色信息
        String role = JwtTokenUtils.getUserRole(token);
        if (username != null){
            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }
}

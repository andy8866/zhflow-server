package com.andy.zhflow.security.token;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class TokenAuthorizationFilter extends BasicAuthenticationFilter {

    private RedisService redisService;
    public TokenAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint,
                                    RedisService redisService) {
        super(authenticationManager,authenticationEntryPoint);

        this.redisService=redisService;
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
        String token = request.getHeader("token");
        // 如果请求头中没有Authorization信息则直接放行了
        if (StringUtils.isEmpty(token) || "null".equals(token)) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));

        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws AuthenticationException {

        // 获取用户信息
        String json = redisService.get(token);
        if(StringUtils.isEmpty(json)){
            throw new AccountExpiredException("token已过期");
        }

        SecurityUser securityUser= JSON.parseObject(json,SecurityUser.class);
        if (securityUser == null) {
            throw new AccountExpiredException("token缓存信息错误");
        }

        return new UsernamePasswordAuthenticationToken(securityUser.getId(), null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_admin"))
        );
    }
}

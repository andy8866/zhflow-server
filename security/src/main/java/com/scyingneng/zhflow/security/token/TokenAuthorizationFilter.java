package com.scyingneng.zhflow.security.token;

import com.alibaba.fastjson.JSON;
import com.scyingneng.zhflow.redis.service.RedisService;
import com.scyingneng.zhflow.security.SecurityUser;
import com.scyingneng.zhflow.vo.AppTokenVO;
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

        if(token.startsWith("USER")){
            SecurityUser securityUser= JSON.parseObject(json,SecurityUser.class);
            if (securityUser == null) {
                throw new AccountExpiredException("user token信息错误");
            }

            return new UsernamePasswordAuthenticationToken(securityUser, null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_admin"))
            );
        } else if (token.startsWith("APP")) {
            AppTokenVO appTokenVO= JSON.parseObject(json, AppTokenVO.class);
            if (appTokenVO == null) {
                throw new AccountExpiredException("app token信息错误");
            }

            return new UsernamePasswordAuthenticationToken(appTokenVO, null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_app"))
            );
        }

        throw new AccountExpiredException("token信息错误");
    }
}

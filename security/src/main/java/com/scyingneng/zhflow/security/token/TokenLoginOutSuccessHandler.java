package com.scyingneng.zhflow.security.token;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.scyingneng.zhflow.redis.service.RedisService;
import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.security.SecurityUser;
import com.scyingneng.zhflow.vo.AppTokenVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenLoginOutSuccessHandler implements LogoutSuccessHandler {

    private RedisService redisService;

    public TokenLoginOutSuccessHandler(RedisService redisService){
        this.redisService=redisService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("token");
        redisService.delete(token);

        ResponseUtil.writeObject(response, ResultResponse.success());
    }
}

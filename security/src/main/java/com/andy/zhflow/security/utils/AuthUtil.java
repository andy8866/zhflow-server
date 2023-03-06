package com.andy.zhflow.security.utils;

import com.andy.zhflow.security.SecurityUser;
import com.andy.zhflow.vo.AppTokenVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static Object getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 认证信息可能为空，因此需要进行判断。
        if (authentication!=null) {
            return authentication.getPrincipal();
        }

        return null;
    }

    public static String getUserId() {
        SecurityUser securityUser=getSecurityUser();
        if(securityUser!=null) return securityUser.getId();

        AppTokenVO appTokenVO=getAppTokenVO();
        if(appTokenVO!=null) return appTokenVO.getUserId();

        return null;
    }

    public static String getAppId() {
        AppTokenVO appTokenVO=getAppTokenVO();
        if(appTokenVO!=null) return appTokenVO.getAppId();

        return null;
    }

    public static SecurityUser getSecurityUser(){
        Object principal =getPrincipal();
        if(principal instanceof SecurityUser){
            return (SecurityUser)principal;
        }

        return null;
    }

    public static AppTokenVO getAppTokenVO(){
        Object principal =getPrincipal();
        if(principal instanceof AppTokenVO){
            return (AppTokenVO)principal;
        }

        return null;
    }
}

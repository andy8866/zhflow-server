package com.andy.zhflow.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static String getUserId() {
        // 获取用户认证信息对象。
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 认证信息可能为空，因此需要进行判断。
        if (authentication!=null) {
            Object principal = authentication.getPrincipal();
            return (String) principal;
        }
        return null;
    }
}

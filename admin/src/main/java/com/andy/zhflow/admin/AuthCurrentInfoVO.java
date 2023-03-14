package com.andy.zhflow.admin;

import lombok.Data;

@Data
public class AuthCurrentInfoVO {
    private String userId;
    private String userName;

    private String appId;
    private String appName;
}

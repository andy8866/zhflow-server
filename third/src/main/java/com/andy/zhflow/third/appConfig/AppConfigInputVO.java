package com.andy.zhflow.third.appConfig;

import lombok.Data;

@Data
public class AppConfigInputVO {
    private String id;
    private String appId;
    private String type;
    private String code;
    private String name;

    private String httpUrlPath;
    private String httpMethod;
}

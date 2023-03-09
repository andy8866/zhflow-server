package com.andy.zhflow.entity;

import lombok.Data;

@Data
public class AppEntity extends BaseEntity {

    private String appId;
    private String appName;
    private String createUserId;
}

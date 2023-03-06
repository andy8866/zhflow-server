package com.andy.zhflow.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class AppEntity extends BaseEntity {

    private String appId;
    private String appName;
    private String createUserId;
}

package com.andy.zhflow.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    private String id;
    private Date createTime;
    private Date updateTime;
}

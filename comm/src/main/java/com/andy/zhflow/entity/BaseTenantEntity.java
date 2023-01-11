package com.andy.zhflow.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseTenantEntity extends BaseEntity{
    private String tenantId;
}

package com.andy.zhflow.entity;

import lombok.Data;

@Data
public class BaseAppEntity extends BaseEntity {
    private String appId;

    public void setBase(Boolean needSetId,String appId) throws Exception {
        this.setAppId(appId);

        super.setBase(needSetId);
    }
}

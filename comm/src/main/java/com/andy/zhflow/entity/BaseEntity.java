package com.andy.zhflow.entity;

import com.andy.zhflow.utils.UUIDUtil;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BaseEntity {
    private String id;
    private Date createTime;
    private Date updateTime;

    public void setBase(){
        setBase(false);
    }
    public void setBase(Boolean needSetId){
        if(needSetId){
            id= UUIDUtil.generate();
        }

        if(createTime==null) {
            createTime = new Date();
        }

        updateTime=new Date();
    }
}

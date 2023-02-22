package com.andy.zhflow.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.andy.zhflow.utils.UUIDUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    private String id;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Boolean isNew=false;

    public void setBase(){
        setBase(false);
    }
    public void setBase(Boolean needSetId){

        if(needSetId && id==null){
            id= NanoIdUtils.randomNanoId();
            isNew=true;
        }

        if(createTime==null) {
            createTime = new Date();
        }

        updateTime=new Date();
    }
}

package com.andy.zhflow.entity;

import com.andy.zhflow.utils.UUIDUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    private String id;
    private Date createTime;
    private Date updateTime;


    @TableField(exist = false)
    private Boolean isNew=false;

    public void setBase(){
        setBase(false);
    }
    public void setBase(Boolean needSetId){

        if(needSetId && id==null){
            id= UUIDUtil.generate();
            isNew=true;
        }

        if(createTime==null) {
            createTime = new Date();
        }

        updateTime=new Date();
    }
}

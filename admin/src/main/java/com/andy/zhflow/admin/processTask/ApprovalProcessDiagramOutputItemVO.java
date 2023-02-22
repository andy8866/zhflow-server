package com.andy.zhflow.admin.processTask;

import lombok.Data;

@Data
public class ApprovalProcessDiagramOutputItemVO {
    private String time;

    private String title;
    private String detail;
    private String color;

    private String icon;

    public void setComplete(){
        color="success";
    }
    public void setDoing(){
        color="info";
    }
    public void setAgree(){
        icon="success";
    }
    public void setReject(){
        icon="fail";
    }
}

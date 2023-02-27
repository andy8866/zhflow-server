package com.andy.zhflow.proc.doProc;

import lombok.Data;

@Data
public class ApprovalProcDiagramOutputItemVO {
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

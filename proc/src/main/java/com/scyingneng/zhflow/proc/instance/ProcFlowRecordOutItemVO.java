package com.scyingneng.zhflow.proc.instance;

import lombok.Data;

@Data
public class ProcFlowRecordOutItemVO {
    private String time;

    private Object title;
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

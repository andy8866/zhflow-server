package com.andy.zhflow.processModel;

import lombok.Data;

@Data
public class ProcessModelInputVO {
    private String id;
    private String name;
    private String content;
    private Boolean isTemplate;
    private String processKey;

    private String appId="0";
}

package com.andy.zhflow.processModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ProcessModelInputVO {
    private String id;
    private String name;
    private String content;

    public boolean getAsTemplate() {
        return asTemplate;
    }

    public void setAsTemplate(boolean asTemplate) {
        this.asTemplate = asTemplate;
    }

    private boolean asTemplate;
    private String processKey;

    private String appId;
}

package com.andy.zhflow.admin.processUi;

import lombok.Data;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProcessUiInputVO {

    private String id;
    private String content;
}

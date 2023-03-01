package com.andy.zhflow.proc.doProc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcRuntimeVO {

    private String processInstanceId;
    private String taskDefinitionKey;

}

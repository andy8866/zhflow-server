package com.andy.zhflow.admin.process;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    public void startProcess(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
    }

}

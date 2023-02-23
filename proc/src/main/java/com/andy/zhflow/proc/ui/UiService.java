package com.andy.zhflow.proc.ui;

import com.andy.zhflow.proc.doProc.DoProcService;
import com.andy.zhflow.proc.BpmnConstant;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UiService {

    @Resource
    private TaskService taskService;

    @Autowired
    private DoProcService doProcService;

    public String getContentByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();

        if(StringUtils.isNotEmpty(task.getFormKey())) {
            return Ui.getById(task.getFormKey()).getContent();
        }

        String processType = doProcService.getProcessType(taskId);
        String code=null;
        switch (processType){
            case BpmnConstant.PROC_TYPE_LEAVE :{
                code="defaultApproval";
                break;
            }
        }

        return Ui.getByCode(code).getContent();
    }
}

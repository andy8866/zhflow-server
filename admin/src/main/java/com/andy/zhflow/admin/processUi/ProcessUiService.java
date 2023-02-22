package com.andy.zhflow.admin.processUi;

import com.andy.zhflow.admin.doProcess.DoProcessService;
import com.andy.zhflow.admin.doProcess.ProcessLeaveService;
import com.andy.zhflow.processModel.BpmnConstant;
import com.andy.zhflow.processUi.ProcessUi;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ProcessUiService {

    @Resource
    private TaskService taskService;

    @Autowired
    private DoProcessService doProcessService;

    public String getContentByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();

        if(StringUtils.isNotEmpty(task.getFormKey())) {
            return ProcessUi.getById(task.getFormKey()).getContent();
        }

        String processType = doProcessService.getProcessType(taskId);
        String code=null;
        switch (processType){
            case BpmnConstant.PROC_TYPE_LEAVE :{
                code="defaultApproval";
                break;
            }
        }

        return ProcessUi.getByCode(code).getContent();
    }
}

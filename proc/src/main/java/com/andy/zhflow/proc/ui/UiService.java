package com.andy.zhflow.proc.ui;

import com.andy.zhflow.proc.definition.DefinitionService;
import com.andy.zhflow.proc.instance.ProcFlowRecordOutItemVO;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class UiService {

    @Resource
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private DefinitionService definitionService;

    public String getContentByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();

        if(task!=null){
            if(StringUtils.isNotEmpty(task.getFormKey())) {
                return Ui.getById(task.getFormKey()).getContent();
            }
        }else {
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            String id= definitionService.getUserTaskFormKey(instance.getProcessDefinitionId(),instance.getTaskDefinitionKey());
            if(StringUtils.isNotEmpty(id)){
                return Ui.getById(id).getContent();
            }
        }

        return null;
    }

    public String getContent(String id, String taskId, String code) {
        if(StringUtils.isNotEmpty(id)) return Ui.getById(id).getContent();
        if(StringUtils.isNotEmpty(taskId)) return getContentByTaskId(taskId);
        if(StringUtils.isNotEmpty(code)) return Ui.getByCode(code).getContent();
        return null;
    }


}

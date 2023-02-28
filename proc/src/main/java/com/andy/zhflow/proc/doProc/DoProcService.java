package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.bean.BeanService;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.model.Model;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import com.andy.zhflow.utils.DateUtil;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableUpdate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DoProcService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BeanService beanService;

    public String getProcessType(String taskId){
        String processDefinitionKey=null;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if(task!=null){
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId()).singleResult();
            if(processDefinition!=null) processDefinitionKey=processDefinition.getKey();
        }else{
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).finished().singleResult();
            processDefinitionKey=instance.getProcessDefinitionKey();
        }

        Model model = Model.getByKey(processDefinitionKey);
        return model.getType();
    }

    public DoProcService getProcessServer(String taskId){

        String type=getProcessType(taskId);
        switch (type){
            case BpmnConstant.PROC_TYPE_APPROVAL:{
                return beanService.getClassBean(ApprovalProcService.class);
            }
        }
        return null;
    }

    public VariableMap initProcVarMap(){
        String userId= UserUtil.getUserId();

        VariableMap variables = Variables.createVariables();
        variables.put(BpmnConstant.ATTR_INITIATOR, userId);
        variables.put(BpmnConstant.VAR_START_USER, userId);
        variables.put(BpmnConstant.VAR_START_USER_NAME, User.getNameById(userId));

        variables.put(BpmnConstant.VAR_PROC_CREATE_TIME, DateUtil.format(new Date()));

        return variables;
    }

    public Map<String,Object> historicDetailVarToMap(List<HistoricDetail> historicDetailList){
        Map<String,Object> map=new HashMap<>();
        for (HistoricDetail historicDetail:historicDetailList){
            HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
            map.put(historicVariableUpdate.getVariableName(),historicVariableUpdate.getValue());
        }
        return map;
    }


}

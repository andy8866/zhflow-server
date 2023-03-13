package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.bean.BeanService;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.user.User;
import com.andy.zhflow.utils.DateUtil;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableUpdate;
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
public class ProcService {

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected BeanService beanService;

    @Autowired
    private IAuthService authService;

    public VariableMap initProcVarMap(){
        String userId= authService.getUserId();

        VariableMap variables = Variables.createVariables();
        variables.put(BpmnConstant.ATTR_INITIATOR, userId);
        variables.put(BpmnConstant.ATTR_INITIATOR_NAME, User.getNameById(userId));

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

    public Map<String,Object> getProcVarByProcessInstanceId(String processInstanceId) {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();

        Map<String, Object> variables = new HashMap<>();
        for (HistoricVariableInstance variableInstance:list){
            variables.put(variableInstance.getName(),variableInstance.getValue());
        }

        return variables;
    }

    public Map<String,Object> getProcVarByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return getProcVarByProcessInstanceId(task.getProcessInstanceId());
    }


}

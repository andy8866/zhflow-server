package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.proc.ApprovalProcDiagramOutputItemVO;
import com.andy.zhflow.bean.BeanService;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.model.Model;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import com.andy.zhflow.utils.DateUtil;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricDetail;
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
    private BeanService beanService;

    public String getProcessType(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        Model model = Model.getByKey(processDefinition.getKey());
        return model.getType();
    }

    public DoProcService getProcessServer(String taskId){

        String type=getProcessType(taskId);
        switch (type){
            case BpmnConstant.PROC_TYPE_LEAVE :{
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

    public List<ApprovalProcDiagramOutputItemVO> getApprovalProcessDiagramData(String taskId) throws Exception {
        throw new Exception("未实现");
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

package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.doProc.ApprovalProcDiagramOutputItemVO;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.doProc.DoProcService;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcTaskService {

    @Resource
    private TaskService taskService;

    @Resource
    private RuntimeService runtimeService;

    @Autowired
    private DoProcService doProcService;

    @Autowired
    protected HistoryService historyService;

    /**
     * 待办任务
     * @param page
     * @param perPage
     * @param userId
     * @return
     */
    public AmisPage<ProcTaskOutputVO> getAgendaTaskList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userId)
                .initializeFormKeys()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public AmisPage<ProcTaskOutputVO> getClaimList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().withoutCandidateUsers()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public void claim(String taskId) {
        taskService.claim(taskId,UserUtil.getUserId());
    }

    private List<ProcTaskOutputVO> convertList(List<Task> list){
        List<ProcTaskOutputVO> outList = ProcTaskOutputVO.convertList(list);

        for (ProcTaskOutputVO outputVO:outList){
            if(StringUtils.isNoneEmpty(outputVO.getAssignee()))  outputVO.setAssigneeName(User.getNameById(outputVO.getAssignee()));
        }
        return outList;
    }

    public void completeTask(String taskId,Map<String,Object> inputVO) throws Exception {
        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if(StringUtils.isNotEmpty(comment)){
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.createComment(taskId,task.getProcessInstanceId(),comment);
        }
        taskService.complete(taskId,inputVO);
    }

    public void assignee(String taskId, String assigneeUserId) {
        taskService.setAssignee(taskId,assigneeUserId);
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

    public Map<String,Object> getTaskLastVar(String processInstanceId,String taskDefinitionKey,Boolean onlyTask) {

        Map<String, Object> lastVarMap=new HashMap<>();

        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey)
                .finished().orderByHistoricTaskInstanceEndTime().desc()
                .list();

        if(list.size()>0){
            HistoricTaskInstance last=list.get(0);
            List<HistoricDetail> historicDetailList = historyService.createHistoricDetailQuery()
                    .processInstanceId(processInstanceId)
                    .activityInstanceId(last.getActivityInstanceId()).list();
            lastVarMap= doProcService.historicDetailVarToMap(historicDetailList);
        }

        if(onlyTask){
            return lastVarMap;
        }else{
            Map<String, Object> procVar = getProcVarByProcessInstanceId(processInstanceId);
            procVar.putAll(lastVarMap);

            return procVar;

        }
    }

    public Map<String,Object> getTaskLastVar(String taskId,boolean onlyTask) {
        String taskDefinitionKey=null;
        String processInstanceId=null;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task!=null){
            taskDefinitionKey=task.getTaskDefinitionKey();
            processInstanceId=task.getProcessInstanceId();
        }else{
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            taskDefinitionKey=instance.getTaskDefinitionKey();
            processInstanceId=instance.getProcessInstanceId();
        }

        return getTaskLastVar(processInstanceId,taskDefinitionKey,onlyTask);
    }

    public List<ApprovalProcDiagramOutputItemVO> getApprovalProcDiagramData(String taskId) throws Exception {
        DoProcService processServer = doProcService.getProcessServer(taskId);
        return processServer.getApprovalProcessDiagramData(taskId);
    }

    public Map<String, Object> getTaskLastVarByTaskDefinitionKey(String taskId,String taskDefinitionKey) {
        String processInstanceId=null;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task!=null){
            processInstanceId = task.getProcessInstanceId();
        }
        else{
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            processInstanceId=instance.getProcessInstanceId();
        }

        return getTaskLastVar(processInstanceId,taskDefinitionKey,false);
    }

    public List<ProcTaskOutputVO> getHistoryCompleteTask(String userId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        List<ProcTaskOutputVO> outList = ProcTaskOutputVO.convertListFromHistory(list);
        return outList;
    }
}

package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.FlowCommentType;
import com.andy.zhflow.proc.doProc.DoProcService;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.task.DelegationState;
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
    private RepositoryService repositoryService;

    @Autowired
    private DoProcService doProcService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected RuntimeService runtimeService;

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



    public void completeTask(String taskId,Map<String,Object> inputVO){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.NORMAL);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.NORMAL,inputVO).toJson());

        if(DelegationState.PENDING.equals(task.getDelegationState())){
            taskService.resolveTask(taskId,inputVO);
        }else{
            taskService.complete(taskId,inputVO);
        }
    }



    public void passTask(String taskId,Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.PASS);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.PASS,inputVO).toJson());

        if(DelegationState.PENDING.equals(task.getDelegationState())){
            taskService.resolveTask(taskId,inputVO);
        }else{
            taskService.complete(taskId,inputVO);
        }
    }

    public void rejectTask(String taskId,Map<String,Object> inputVO) throws Exception {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.REJECT);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.REJECT,inputVO).toJson());

        taskService.setVariables(taskId,inputVO);

        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"审批拒绝");
    }

    public void delegateTask(String taskId,Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.DELEGATE);

        String toUserId= (String) inputVO.getOrDefault("toUserId","");
        StringBuilder commentBuilder = new StringBuilder(User.getNameById(UserUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.DELEGATE,inputVO).toJson());

        taskService.setOwner(taskId,UserUtil.getUserId());
        taskService.delegateTask(taskId, toUserId);
    }

    public void transferTask(String taskId,Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.TRANSFER);

        String toUserId= (String) inputVO.getOrDefault("toUserId","");
        StringBuilder commentBuilder = new StringBuilder(User.getNameById(UserUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.TRANSFER,inputVO).toJson());

        taskService.setOwner(taskId,UserUtil.getUserId());
        taskService.setAssignee(taskId,toUserId);
    }

    public void rebackTask(String taskId,Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.REBACK);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.REBACK,inputVO).toJson());

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();

        HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().asc().list().get(0);

        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
                .cancelAllForActivity(historicTaskInstance.getActivityInstanceId())
                .startBeforeActivity(historicActivityInstance.getActivityId())
                .execute();
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

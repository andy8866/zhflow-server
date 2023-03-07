package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.FlowCommentType;
import com.andy.zhflow.proc.copy.CopyService;
import com.andy.zhflow.proc.definition.DefinitionService;
import com.andy.zhflow.proc.task.ProcTaskOutVO;
import com.andy.zhflow.proc.task.TaskCommentVO;
import com.andy.zhflow.security.utils.AuthUtil;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcTaskService extends ProcService{

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Autowired
    private ProcService procService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected CopyService copyService;

    @Autowired
    protected DefinitionService definitionService;

    public List<ProcTaskOutVO> convertTaskOutList(List<Task> list){
        List<ProcTaskOutVO> outList = ProcTaskOutVO.convertList(list);

        for (ProcTaskOutVO outputVO:outList){
            if(StringUtils.isNoneEmpty(outputVO.getAssignee()))  outputVO.setAssigneeName(User.getNameById(outputVO.getAssignee()));
        }
        return outList;
    }

    public ProcRuntimeVO getProcRuntimeVO(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task!=null){
            return new ProcRuntimeVO(task.getProcessInstanceId(),task.getTaskDefinitionKey(),task.getProcessDefinitionId());
        }else{
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            return new ProcRuntimeVO(instance.getProcessInstanceId(),instance.getTaskDefinitionKey(),instance.getProcessDefinitionId());
        }
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

        copyService.makeCopy(taskId,inputVO);
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
            lastVarMap= procService.historicDetailVarToMap(historicDetailList);
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
        ProcRuntimeVO procRuntimeVO = getProcRuntimeVO(taskId);
        return getTaskLastVar(procRuntimeVO.getProcessInstanceId(),procRuntimeVO.getTaskDefinitionKey(),onlyTask);
    }

    public Map<String, Object> getStartTaskLastVar(String taskId) {
        ProcRuntimeVO procRuntimeVO = getProcRuntimeVO(taskId);
        String firstTaskDefinitionKey = definitionService.getFirstTaskDefinitionKey(procRuntimeVO.getProcessDefinitionId());
        return getTaskLastVar(procRuntimeVO.getProcessInstanceId(),firstTaskDefinitionKey,false);
    }

    public List<ProcTaskOutVO> getHistoryCompleteTask(String userId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .tenantIdIn(AuthUtil.getAppId())
                .taskAssignee(userId).finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        return ProcTaskOutVO.convertListFromHistory(list);
    }
}

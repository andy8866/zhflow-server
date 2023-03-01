package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.FlowCommentType;
import com.andy.zhflow.proc.copy.CopyService;
import com.andy.zhflow.proc.task.ProcTaskOutVO;
import com.andy.zhflow.proc.task.TaskCommentVO;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProcUserTaskService extends ProcService {

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    protected RuntimeService runtimeService;


    @Autowired
    protected ProcTaskService procTaskService;

    @Autowired
    protected CopyService copyService;

    @Autowired
    protected IUserService userService;

    /**
     * 待办任务
     * @param page
     * @param perPage
     * @param userId
     * @return
     */
    public AmisPage<ProcTaskOutVO> getAgendaList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userId)
                .initializeFormKeys()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutVO> outList =procTaskService.convertTaskOutList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public AmisPage<ProcTaskOutVO> getClaimList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().withoutCandidateUsers()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutVO> outList =procTaskService.convertTaskOutList(list);

        return AmisPage.transitionPage(outList,total);
    }


    public void claim(String taskId) {
        taskService.claim(taskId,UserUtil.getUserId());
    }

    public void pass(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE, FlowCommentType.PASS);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.PASS,inputVO).toJson());

        if(DelegationState.PENDING.equals(task.getDelegationState())){
            taskService.resolveTask(taskId,inputVO);
        }else{
            taskService.complete(taskId,inputVO);
        }

        copyService.makeCopy(taskId,inputVO);
    }

    public void reject(String taskId, Map<String,Object> inputVO) throws Exception {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.REJECT);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.REJECT,inputVO).toJson());

        taskService.setVariables(taskId,inputVO);

        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"审批拒绝");

        copyService.makeCopy(taskId,inputVO);
    }

    public void delegate(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.DELEGATE);

        String toUserId= (String) inputVO.getOrDefault("toUserId","");
        StringBuilder commentBuilder = new StringBuilder(userService.getNameById(UserUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.DELEGATE,inputVO).toJson());

        taskService.setOwner(taskId,UserUtil.getUserId());
        taskService.delegateTask(taskId, toUserId);

        copyService.makeCopy(taskId,inputVO);
    }

    public void transfer(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.TRANSFER);

        String toUserId= (String) inputVO.getOrDefault("toUserId","");
        StringBuilder commentBuilder = new StringBuilder(userService.getNameById(UserUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.TRANSFER,inputVO).toJson());

        taskService.setOwner(taskId,UserUtil.getUserId());
        taskService.setAssignee(taskId,toUserId);

        copyService.makeCopy(taskId,inputVO);
    }

    public void reback(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.REBACK);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.REBACK,inputVO).toJson());

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();

        HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list().get(0);

        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
                .cancelAllForActivity(historicTaskInstance.getActivityInstanceId())
                .startBeforeActivity(historicActivityInstance.getActivityId())
                .execute();

        copyService.makeCopy(taskId,inputVO);
    }


    public void assignee(String taskId, String assigneeUserId) {
        taskService.setAssignee(taskId,assigneeUserId);
    }
}

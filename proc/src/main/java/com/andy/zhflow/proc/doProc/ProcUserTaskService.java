package com.andy.zhflow.proc.doProc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.BpmnUtil;
import com.andy.zhflow.proc.FlowCommentType;
import com.andy.zhflow.proc.copy.CopyService;
import com.andy.zhflow.proc.task.ProcTaskOutVO;
import com.andy.zhflow.proc.task.TaskCommentVO;
import com.andy.zhflow.security.utils.AuthUtil;
import com.andy.zhflow.service.dept.IDeptService;
import com.andy.zhflow.service.role.IRoleService;
import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Autowired
    protected IRoleService roleService;

    @Autowired
    protected IDeptService deptService;


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

        TaskQuery taskQuery = taskService.createTaskQuery().initializeFormKeys().orderByTaskCreateTime().desc()
                .or().taskCandidateUser(userId).taskCandidateGroupIn(getCandidateGroup(userId)).endOr();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutVO> outList =procTaskService.convertTaskOutList(list);

        return AmisPage.transitionPage(outList,total);
    }


    public void claim(String taskId) {
        taskService.claim(taskId, AuthUtil.getUserId());
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
        StringBuilder commentBuilder = new StringBuilder(userService.getNameById(AuthUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.DELEGATE,inputVO).toJson());

        taskService.setOwner(taskId, AuthUtil.getUserId());
        taskService.delegateTask(taskId, toUserId);

        copyService.makeCopy(taskId,inputVO);
    }

    public void transfer(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.TRANSFER);

        String toUserId= (String) inputVO.getOrDefault("toUserId","");
        StringBuilder commentBuilder = new StringBuilder(userService.getNameById(AuthUtil.getUserId())).append("->").append(User.getNameById(toUserId));

        String comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");
        if (StringUtils.isNotBlank(comment)) commentBuilder.append(": ").append(comment);
        inputVO.put(BpmnConstant.VAR_COMMENT,commentBuilder.toString());

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.TRANSFER,inputVO).toJson());

        taskService.setOwner(taskId, AuthUtil.getUserId());
        taskService.setAssignee(taskId,toUserId);

        copyService.makeCopy(taskId,inputVO);
    }

    public void reback(String taskId, Map<String,Object> inputVO) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        inputVO.put(BpmnConstant.VAR_COMMENT_TYPE,FlowCommentType.REBACK);

        taskService.createComment(taskId,task.getProcessInstanceId(), TaskCommentVO.createComment(FlowCommentType.REBACK,inputVO).toJson());

        HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc()
                .list().get(0);

        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
                .cancelAllForActivity(task.getTaskDefinitionKey())
                .startBeforeActivity(historicActivityInstance.getActivityId())
                .execute();

        copyService.makeCopy(taskId,inputVO);
    }


    public void assignee(String taskId, String assigneeUserId) {
        taskService.setAssignee(taskId,assigneeUserId);
    }

    public void setSuperiorUser(DelegateTask task){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().desc()
                .list();

        String userid=list.get(0).getAssignee();
        String superiorUserId=userService.getSuperiorUserId(userid);
        task.setAssignee(superiorUserId);
    }

    public HashSet<String> getMultiInstanceUserIds(DelegateExecution execution) {
        HashSet<String> candidateUserIds = new LinkedHashSet<>();
        ModelElementInstance flowElement=null;
        if(((ExecutionEntity)execution).getActivity()!=null){
            String activityId=((ExecutionEntity)execution).getActivity().getActivities().get(0).getActivityId();
            flowElement= execution.getBpmnModelInstance().getModelElementById(activityId);
        }else{
            return (HashSet<String>) execution.getProcessInstance().getVariable(BpmnConstant.VAR_MULTI_INSTANCE_LOOP_USER_LIST);
        }

        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            String dataType = BpmnUtil.getAttributeValue(userTask,BpmnConstant.BPMN_CUSTOM_DATA_TYPE);
            if (BpmnConstant.BPMN_CUSTOM_DATA_TYPE_USERS.equals(dataType) && CollUtil.isNotEmpty(userTask.getCamundaCandidateUsersList())) {
                candidateUserIds.addAll(userTask.getCamundaCandidateUsersList());
            }
            else if (CollUtil.isNotEmpty(userTask.getCamundaCandidateGroupsList())) {
                List<String> groups = userTask.getCamundaCandidateGroupsList();
                if (BpmnConstant.BPMN_CUSTOM_DATA_TYPE_ROLES.equals(dataType)) {
                    groups.forEach(item -> {
                        String roleId=item.replace(BpmnConstant.CANDIDATE_ROLE_GROUP_PREFIX,"");
                        List<String> userIds = roleService.getUserIdsByRoleId(roleId);
                        candidateUserIds.addAll(userIds);
                    });
                } else if (BpmnConstant.BPMN_CUSTOM_DATA_TYPE_DEPTS.equals(dataType)) {
                    groups.forEach(item -> {
                        String deptId=item.replace(BpmnConstant.CANDIDATE_DEPT_GROUP_PREFIX,"");
                        List<String> userIds = deptService.getUserIdsByDeptId(deptId);
                        candidateUserIds.addAll(userIds);
                    });
                }
            }
        }

        execution.getProcessInstance().setVariable(BpmnConstant.VAR_MULTI_INSTANCE_LOOP_USER_LIST,candidateUserIds);
        return candidateUserIds;
    }

    public  List<String> getCandidateGroup(String userId) {
        List<String> list = new ArrayList<>();

        List<String> userRoleIds = roleService.getUserRoleIds(userId);
        if (ObjectUtil.isNotEmpty(userRoleIds)) {
            userRoleIds.forEach(item -> list.add(BpmnConstant.CANDIDATE_ROLE_GROUP_PREFIX + item));
        }

        List<String> userDeptIds = deptService.getUserDeptIds(userId);
        if (ObjectUtil.isNotEmpty(userDeptIds)) {
            userDeptIds.forEach(item -> list.add(BpmnConstant.CANDIDATE_DEPT_GROUP_PREFIX + item));
        }

        return list;
    }
}

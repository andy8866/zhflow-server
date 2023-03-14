package com.andy.zhflow.proc.instance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.BpmnUtil;
import com.andy.zhflow.proc.doProc.ProcService;
import com.andy.zhflow.security.utils.AuthService;
import com.andy.zhflow.service.dept.IDeptService;
import com.andy.zhflow.service.role.IRoleService;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.service.thirdApp.IThirdAppService;
import com.andy.zhflow.service.uiPage.IUiPageService;
import com.andy.zhflow.user.User;
import com.andy.zhflow.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricIdentityLinkLog;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.commons.utils.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

@Component
public class InstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    protected IdentityService identityService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ProcService procService;

    @Resource
    protected HistoryService historyService;

    @Resource
    protected TaskService taskService;

    @Autowired
    protected IUiPageService uiPageService;

    @Autowired
    protected IRoleService roleService;

    @Autowired
    protected IDeptService deptService;

    @Autowired
    protected IAuthService authService;

    @Autowired
    protected IThirdAppService thirdAppService;

    public void startProc(String procKey, Map<String,Object> vars) throws Exception {
        String userId= authService.getUserId();
        identityService.setAuthenticatedUserId(userId);

        VariableMap variableMap = procService.initProcVarMap();
        if(vars!=null) variableMap.putAll(vars);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procKey).tenantIdIn(authService.getAppId()).latestVersion().singleResult();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),variableMap);

        variableMap.put("procInsId",processInstance.getProcessInstanceId());
        thirdAppService.startProc(authService.getAppId(),variableMap);
    }

    public AmisPage<InstanceOutputVO> getList(Integer page, Integer perPage) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        Long total=processInstanceQuery.count();

        List<ProcessInstance> list = processInstanceQuery.listPage((page-1) * perPage, perPage);

        List<InstanceOutputVO> outList = InstanceOutputVO.convertList(list);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        for (InstanceOutputVO outputVO:outList){
            ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(outputVO.getProcessDefinitionId()).singleResult();
            Deployment deployment = deploymentQuery.deploymentId(processDefinition.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }


    public void cancelProc(String id) {
        runtimeService.deleteProcessInstance(id,"取消");
    }

    public void getDetail(String procInsId, String taskId) {
        if(StringUtils.isEmpty(procInsId) && StringUtils.isNotEmpty(taskId)){
            procInsId=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
        }
    }

    public ProcViewerVO getProcViewer(String procInsId){
        // 构建查询条件
        List<HistoricActivityInstanceEntity> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId).list()
                .stream().map(item -> (HistoricActivityInstanceEntity)item)
                .sorted(Comparator.comparing(HistoricActivityInstanceEntity::getStartTime)
                .thenComparing(HistoricActivityInstanceEntity::getSequenceCounter))
                .toList();
        ;
        if (activityInstanceList.size()==0) {
            return new ProcViewerVO();
        }

        String processDefinitionId=activityInstanceList.get(0).getProcessDefinitionId();
        InputStream inputStream = repositoryService.getProcessModel(processDefinitionId);
        String xmlData=IoUtil.inputStreamAsString(inputStream);

        HistoricActivityInstance lastItem=activityInstanceList.get(activityInstanceList.size()-1);

        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();

        for (int i = 0; i < activityInstanceList.size(); i++) {
            HistoricActivityInstance item=activityInstanceList.get(i);
            if(item.getActivityId().equals(lastItem.getActivityId())) break;

            if (BpmnConstant.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        }

        // 当前节点
        Set<String> unfinishedTaskSet=new HashSet<>();
        if(lastItem.getEndTime()==null) unfinishedTaskSet.add(lastItem.getActivityId());
        else finishedTaskSet.add(lastItem.getActivityId());

        // DFS 查询未通过的元素集合
        BpmnModelInstance bpmnModel = repositoryService.getBpmnModelInstance(processDefinitionId);
        Set<String> rejectedSet = BpmnUtil.dfsFindRejects(bpmnModel, unfinishedTaskSet, finishedSequenceFlowSet, finishedTaskSet);

        return new ProcViewerVO(xmlData,historyProcNodeList(procInsId),finishedTaskSet, finishedSequenceFlowSet, unfinishedTaskSet, rejectedSet);
    }

    public List<ProcNodeVO> historyProcNodeListByTaskId(String taskId) {
        String procInsId="";

        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task!=null){
            procInsId=task.getProcessInstanceId();
        }else{
            HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            procInsId=instance.getProcessInstanceId();
        }
        return historyProcNodeList(procInsId);
    }

    public List<ProcNodeVO> historyProcNodeList(String procInsId) {
        List<HistoricActivityInstanceEntity> historicActivityInstanceList =  historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .list()
                .stream().map(item -> (HistoricActivityInstanceEntity)item)
                .sorted(Comparator.comparing(HistoricActivityInstanceEntity::getStartTime,Comparator.reverseOrder())
                        .thenComparing(HistoricActivityInstanceEntity::getSequenceCounter,Comparator.reverseOrder())
                )
                .toList();

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInsId)
                .singleResult();

        List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
        List<ProcCommentOutVO> procComments = ProcCommentOutVO.convertList(commentList);

        List<ProcNodeVO> elementVoList = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if(BpmnConstant.ELEMENT_EVENT_START.equals(activityInstance.getActivityType()) &&
                    BpmnConstant.ELEMENT_EVENT_END.equals(activityInstance.getActivityType()) &&
                    BpmnConstant.ELEMENT_TASK_USER.equals(activityInstance.getActivityType()))
            {
                continue;
            }

            ProcNodeVO elementVo = new ProcNodeVO();
            elementVo.setProcDefId(activityInstance.getProcessDefinitionId());
            elementVo.setActivityId(activityInstance.getActivityId());
            elementVo.setActivityName(activityInstance.getActivityName());
            elementVo.setActivityType(activityInstance.getActivityType());
            elementVo.setCreateTime(activityInstance.getStartTime());
            elementVo.setEndTime(activityInstance.getEndTime());
            if (activityInstance.getDurationInMillis()!=null) {
                elementVo.setDuration(DateUtil.formatBetween(activityInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            }

            if (BpmnConstant.ELEMENT_EVENT_START.equals(activityInstance.getActivityType())) {
                if (historicProcessInstance!=null) {
                    String userId = historicProcessInstance.getStartUserId();
                    elementVo.setAssigneeId(userId);
                    elementVo.setAssigneeName(User.getNameById(userId));
                }
            } else if (BpmnConstant.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())) {
                if (StringUtils.isNotBlank(activityInstance.getAssignee())) {
                    String userId=activityInstance.getAssignee();
                    elementVo.setAssigneeId(userId);
                    elementVo.setAssigneeName(User.getNameById(userId));
                }
                // 展示审批人员
                List<HistoricIdentityLinkLog> linksForTask = historyService.createHistoricIdentityLinkLogQuery()
                        .taskId(activityInstance.getTaskId())
                        .list();
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLinkLog identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            stringBuilder.append(User.getNameById(identityLink.getUserId())).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(BpmnConstant.CANDIDATE_ROLE_GROUP_PREFIX)) {
                                String roleId = StringUtils.stripStart(identityLink.getGroupId(), BpmnConstant.CANDIDATE_ROLE_GROUP_PREFIX);
                                stringBuilder.append(roleService.getRoleNameById(roleId)).append(",");
                            } else if (identityLink.getGroupId().startsWith(BpmnConstant.CANDIDATE_DEPT_GROUP_PREFIX)) {
                                String deptId = StringUtils.stripStart(identityLink.getGroupId(), BpmnConstant.CANDIDATE_DEPT_GROUP_PREFIX);
                                stringBuilder.append(deptService.getDeptNameById(deptId)).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    elementVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(procComments)) {
                    List<ProcCommentOutVO> comments = new ArrayList<>();
                    for (ProcCommentOutVO comment : procComments) {

                        if (comment.getTaskId().equals(activityInstance.getTaskId())) {
                            comments.add(comment);
                        }
                    }
                    elementVo.setCommentList(comments);
                }
            }
            elementVoList.add(elementVo);
        }
        return elementVoList;
    }

    public List<ProcFlowRecordOutItemVO> getProcFlowRecord(String taskId,String procInsId) {
        List<ProcFlowRecordOutItemVO> list=new ArrayList<>();

        String procFlowRecordItemContent = uiPageService.getContentByCode("procFlowRecordItem");

        List<ProcNodeVO> procNodeVOList=new ArrayList<>() ;

        if(StringUtils.isNotEmpty(taskId)) procNodeVOList= historyProcNodeListByTaskId(taskId);
        if(StringUtils.isNotEmpty(procInsId)) procNodeVOList= historyProcNodeList(procInsId);

        List<String> typeList= Arrays.asList(
                ActivityTypes.START_EVENT,
                ActivityTypes.END_EVENT_NONE,
                ActivityTypes.TASK_USER_TASK
        );

        for (int i = 0; i < procNodeVOList.size(); i++) {
            ProcNodeVO procNodeVO=procNodeVOList.get(i);

            if(!typeList.contains(procNodeVO.getActivityType())) continue;

            ProcFlowRecordOutItemVO itemVO=new ProcFlowRecordOutItemVO();

            if(StringUtils.isNotEmpty(procNodeVO.getActivityName())){
                itemVO.setTime(procNodeVO.getActivityName());
            }else{
                if(ActivityTypes.START_EVENT.equals(procNodeVO.getActivityType())){
                    itemVO.setTime("开始");
                } else if (ActivityTypes.END_EVENT_NONE.equals(procNodeVO.getActivityType())) {
                    itemVO.setTime("结束");
                }
            }

            if(procNodeVO.getEndTime()!=null) itemVO.setComplete();

            JSONObject jsonObject = JSON.parseObject(procFlowRecordItemContent);
            jsonObject.put("data",procNodeVO);

            itemVO.setTitle(jsonObject);

            list.add(itemVO);
        }

        return list;
    }
}

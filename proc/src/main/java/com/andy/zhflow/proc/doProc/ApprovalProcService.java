package com.andy.zhflow.proc.doProc;

import com.andy.zhflow.proc.ApprovalProcDiagramOutputItemVO;
import com.andy.zhflow.proc.BpmnModelUtil;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.user.User;
import com.andy.zhflow.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApprovalProcService extends DoProcService {

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;



    @Override
    public List<ApprovalProcDiagramOutputItemVO> getApprovalProcessDiagramData(String taskId) throws Exception {
        List<ApprovalProcDiagramOutputItemVO> list=new ArrayList<>();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId=task.getProcessInstanceId();
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(task.getProcessDefinitionId());

        ModelElementType type = bpmnModelInstance.getModel().getType(UserTask.class);
        List<FlowNode> flowList = BpmnModelUtil.getFlowList(bpmnModelInstance, type);
        for (int i = 0; i < flowList.size(); i++) {
            FlowNode flowNode= flowList.get(i);

            ApprovalProcDiagramOutputItemVO itemVO=new ApprovalProcDiagramOutputItemVO();
            itemVO.setTime(flowNode.getName());

            StringBuilder titleBuilder=new StringBuilder();
            StringBuilder detailBuilder=new StringBuilder();

            Task findTask=null;
            HistoricTaskInstance historicTaskInstance=null;
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                    .taskDefinitionKey(flowNode.getId())
                    .orderByHistoricActivityInstanceStartTime().desc()
                    .list();
            if(historicTaskInstanceList.size()>0) historicTaskInstance=historicTaskInstanceList.get(0);
            if(historicTaskInstance!=null){
                if(historicTaskInstance.getEndTime()!=null){
                    itemVO.setComplete();
                    detailBuilder.append(DateUtil.format(historicTaskInstance.getEndTime())).append(System.lineSeparator());
                }else{
                    itemVO.setDoing();
                    detailBuilder.append(DateUtil.format(historicTaskInstance.getStartTime())).append(System.lineSeparator());
                }

                if(StringUtils.isNotEmpty(historicTaskInstance.getAssignee())){
                    titleBuilder.append(User.getNameById( historicTaskInstance.getAssignee())).append(" ");
                }else {
                    titleBuilder.append("未指定审批人").append(" ");
                }

                List<HistoricDetail> historicDetailList = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId)
                        .activityInstanceId(historicTaskInstance.getActivityInstanceId()).list();
                Map<String, Object> historicDetailVarToMap = historicDetailVarToMap(historicDetailList);
                if(historicDetailVarToMap.containsKey(BpmnConstant.VAR_APPROVAL)){
                    if((boolean) historicDetailVarToMap.get(BpmnConstant.VAR_APPROVAL)){
                        titleBuilder.append("通过");
                        itemVO.setAgree();
                    }
                    else{
                        titleBuilder.append("未通过");
                        itemVO.setReject();
                    }
                    detailBuilder.append(historicDetailVarToMap.get(BpmnConstant.VAR_REASON)).append(System.lineSeparator());
                }
            }

            if(historicTaskInstance==null){
                findTask = taskService.createTaskQuery().processInstanceId(processInstanceId)
                        .taskDefinitionKey(flowNode.getId()).singleResult();

                if(findTask!=null){
                    itemVO.setDoing();
                    detailBuilder.append(DateUtil.format(findTask.getCreateTime())).append(System.lineSeparator());

                    if(StringUtils.isNotEmpty(findTask.getAssignee())){
                        titleBuilder.append(User.getNameById( findTask.getAssignee()));
                    }else{
                        titleBuilder.append("未指定审批人").append(" ");
                    }
                }
            }

            if(historicTaskInstance==null && findTask==null){
                //未创建

                String assignee=flowNode.getAttributeValueNs( flowNode.getElementType().getAttribute(BpmnConstant.ATTR_ASSIGNEE).getNamespaceUri(),
                        BpmnConstant.ATTR_ASSIGNEE);

                if(StringUtils.isNotEmpty(assignee)){
                    titleBuilder.append(User.getNameById(assignee));
                }else{
                    titleBuilder.append("未指定审批人").append(" ");
                }
            }

            itemVO.setTitle(titleBuilder.toString());
            itemVO.setDetail(detailBuilder.toString());
            list.add(itemVO);
        }

        return list;
    }
}

package com.andy.zhflow.proc.definition;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.BpmnConstant;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.List;

@Component
public class DefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    public AmisPage<DefinitionOutputVO> getList(Integer page, Integer perPage) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentTime().desc()
                .latestVersion();

        Long total=processDefinitionQuery.count();

        List<ProcessDefinition> list = processDefinitionQuery.listPage((page-1) * perPage, perPage);

        List<DefinitionOutputVO> outList = DefinitionOutputVO.convertList(list);

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        for (DefinitionOutputVO outputVO:outList){
            Deployment deployment = deploymentQuery.deploymentId(outputVO.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());

            if(outputVO.getHasStartFormKey()){
                outputVO.setStartFormKey(getStarFormKey(outputVO.getId()));
            }
        }

        return AmisPage.transitionPage(outList,total);
    }

    public String getStarFormKey(String processDefinitionId) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);
        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        for (StartEvent startEvent:startEvents){
            if(StringUtils.isNotEmpty(startEvent.getCamundaFormKey())){
                return startEvent.getCamundaFormKey();
            }
        }

        return null;
    }

    public String getUserTaskFormKey(String processDefinitionKey,String taskDefinitionKey) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionKey);
        UserTask userTask =(UserTask) bpmnModelInstance.getModelElementById(taskDefinitionKey);
        if(userTask!=null){
            return userTask.getCamundaFormKey();
        }
        return null;
    }

    public String getFirstTaskDefinitionKey(String processDefinitionKey) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionKey);
        StartEvent startEvent = bpmnModelInstance.getModelElementsByType(StartEvent.class).stream().toList().get(0);
        FlowNode target = startEvent.getOutgoing().stream().toList().get(0).getTarget();
        if(target.getElementType().getTypeName().equals(BpmnConstant.ELEMENT_TASK_USER)){
            return target.getId();
        }

        return null;
    }


    public String getStartKey(String processDefinitionKey) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionKey);
        StartEvent startEvent = bpmnModelInstance.getModelElementsByType(StartEvent.class).stream().toList().get(0);
        return startEvent.getId();
    }
}

package com.andy.zhflow.admin.processInstance;

import com.andy.zhflow.amis.AmisPage;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessInstanceService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public void startProcess(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
    }

    public AmisPage<ProcessInstanceOutputVO> getList(Integer page, Integer perPage) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        Long total=processInstanceQuery.count();

        List<ProcessInstance> list = processInstanceQuery.listPage((page-1) * perPage, perPage);

        List<ProcessInstanceOutputVO> outList = ProcessInstanceOutputVO.convertList(list);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        for (ProcessInstanceOutputVO outputVO:outList){
            ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(outputVO.getProcessDefinitionId()).singleResult();
            Deployment deployment = deploymentQuery.deploymentId(processDefinition.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }
}

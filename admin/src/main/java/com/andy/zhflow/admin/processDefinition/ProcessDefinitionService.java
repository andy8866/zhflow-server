package com.andy.zhflow.admin.processDefinition;

import com.andy.zhflow.amis.AmisPage;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    public AmisPage<ProcessDefinitionOutputVO> getList(Integer page, Integer perPage) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion();

        Long total=processDefinitionQuery.count();

        List<ProcessDefinition> list = processDefinitionQuery.listPage((page-1) * perPage, perPage);

        List<ProcessDefinitionOutputVO> outList = ProcessDefinitionOutputVO.convertList(list);

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        for (ProcessDefinitionOutputVO outputVO:outList){
            Deployment deployment = deploymentQuery.deploymentId(outputVO.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }
}

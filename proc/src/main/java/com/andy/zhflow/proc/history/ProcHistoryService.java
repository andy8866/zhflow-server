package com.andy.zhflow.proc.history;

import com.andy.zhflow.amis.AmisPage;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcHistoryService {

    @Autowired
    private org.camunda.bpm.engine.HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public AmisPage<ProcHistoryOutputVO> getList(Integer page, Integer perPage) {

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished();

        Long total=query.count();

        List<HistoricProcessInstance> list = query.listPage((page-1) * perPage, perPage);

        List<ProcHistoryOutputVO> outList = ProcHistoryOutputVO.convertList(list);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        for (ProcHistoryOutputVO outputVO:outList){
            ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(outputVO.getProcessDefinitionId()).singleResult();
            Deployment deployment = deploymentQuery.deploymentId(processDefinition.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }
}

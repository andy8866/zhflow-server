package com.scyingneng.zhflow.proc.history;

import com.scyingneng.zhflow.amis.AmisPage;
import com.scyingneng.zhflow.service.security.IAuthService;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
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
    private IAuthService authService;

    @Autowired
    private RepositoryService repositoryService;

    public AmisPage<ProcHistoryProcOutVO> getList(Integer page, Integer perPage,
                                                  String userId) {

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .tenantIdIn(authService.getAppId())
                .orderByProcessInstanceStartTime().desc();

        if(StringUtils.isNotEmpty(userId)) query.startedBy(userId);

        Long total=query.count();

        List<HistoricProcessInstance> list = query.listPage((page-1) * perPage, perPage);

        List<ProcHistoryProcOutVO> outList = ProcHistoryProcOutVO.convertList(list);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        for (ProcHistoryProcOutVO outputVO:outList){
            ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(outputVO.getProcessDefinitionId()).singleResult();
            Deployment deployment = deploymentQuery.deploymentId(processDefinition.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }
}

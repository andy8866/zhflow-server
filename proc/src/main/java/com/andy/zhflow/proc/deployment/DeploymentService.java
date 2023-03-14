package com.andy.zhflow.proc.deployment;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.security.utils.AuthService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeploymentService {

    @Autowired
    private AuthService authService;

    @Autowired
    private RepositoryService repositoryService;

    public AmisPage<DeploymentOutputVO> getList(Integer page, Integer perPage) {

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery().tenantIdIn(authService.getAppId());

        Long total=deploymentQuery.count();

        List<Deployment> list = deploymentQuery.listPage((page-1) * perPage, perPage);

        List<DeploymentOutputVO> outList = DeploymentOutputVO.convertList(list);

        return AmisPage.transitionPage(outList,total);
    }
}

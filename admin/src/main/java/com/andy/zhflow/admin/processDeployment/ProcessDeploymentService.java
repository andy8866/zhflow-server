package com.andy.zhflow.admin.processDeployment;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.amis.AmisPage;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessDeploymentService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AppUserService appUserService;

    public AmisPage<ProcessDeploymentOutputVO> getList(Integer page, Integer perPage) {
        String appId=appUserService.getSelectAppId();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery()
                .tenantIdIn(appId);

        Long total=deploymentQuery.count();

        List<Deployment> list = deploymentQuery.listPage((page-1) * perPage, perPage);

        List<ProcessDeploymentOutputVO> outList = ProcessDeploymentOutputVO.convertList(list);

        return AmisPage.transitionPage(outList,total);
    }
}

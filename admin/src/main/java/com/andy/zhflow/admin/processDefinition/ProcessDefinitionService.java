package com.andy.zhflow.admin.processDefinition;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.amis.AmisPage;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessDefinitionService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AppUserService appUserService;

    public AmisPage<ProcessDefinitionOutputVO> getList(Integer page, Integer perPage) {
        String appId=appUserService.getSelectAppId();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .tenantIdIn(appId)
                .latestVersion();

        Long total=processDefinitionQuery.count();

        List<ProcessDefinition> list = processDefinitionQuery.listPage((page-1) * perPage, perPage);

        List<ProcessDefinitionOutputVO> outList = ProcessDefinitionOutputVO.convertList(list);

        return AmisPage.transitionPage(outList,total);
    }
}

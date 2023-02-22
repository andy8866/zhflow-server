package com.andy.zhflow.admin.processInstance;

import com.andy.zhflow.admin.doProcess.DoProcessService;
import com.andy.zhflow.admin.doProcess.ProcessLeaveService;
import com.andy.zhflow.admin.user.UserService;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.security.utils.UserUtil;
import org.camunda.bpm.engine.IdentityService;
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

import javax.annotation.Resource;
import java.util.*;

@Component
public class ProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    protected IdentityService identityService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DoProcessService doProcessService;

    public void startProcess(String processKey) {
        String userId=UserUtil.getUserId();
        identityService.setAuthenticatedUserId(userId);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey,doProcessService.initProcVarMap());
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

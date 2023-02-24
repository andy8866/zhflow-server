package com.andy.zhflow.proc.instance;

import com.andy.zhflow.proc.doProc.DoProcService;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class InstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    protected IdentityService identityService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DoProcService doProcService;

    public void startProc(String procKey, Map<String,Object> vars) {
        String userId=UserUtil.getUserId();
        identityService.setAuthenticatedUserId(userId);

        VariableMap variableMap = doProcService.initProcVarMap();
        if(vars!=null) variableMap.putAll(vars);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procKey,variableMap );
    }

    public AmisPage<InstanceOutputVO> getList(Integer page, Integer perPage) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        Long total=processInstanceQuery.count();

        List<ProcessInstance> list = processInstanceQuery.listPage((page-1) * perPage, perPage);

        List<InstanceOutputVO> outList = InstanceOutputVO.convertList(list);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        for (InstanceOutputVO outputVO:outList){
            ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(outputVO.getProcessDefinitionId()).singleResult();
            Deployment deployment = deploymentQuery.deploymentId(processDefinition.getDeploymentId()).singleResult();
            outputVO.setName(deployment.getName());
        }

        return AmisPage.transitionPage(outList,total);
    }


    public void cancelProc(String id) {
        runtimeService.deleteProcessInstance(id,"取消");
    }
}

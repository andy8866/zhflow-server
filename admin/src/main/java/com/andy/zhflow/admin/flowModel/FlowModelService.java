package com.andy.zhflow.admin.flowModel;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.app.App;
import com.andy.zhflow.flowModel.FlowModel;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.utils.UserUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class FlowModelService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AppUserService appUserService;

    public App getSelectApp() {
        String appId = (String) redisService.get("selectApp." + UserUtil.getUserId());
        if(StringUtils.isEmpty(appId)){
            return null;
        }

        return App.getById(appId);
    }

    public String getSelectAppId() {
       App app=getSelectApp();
       if(app!=null){
           return app.getId();
       }

       return null;
    }

    public void setSelectApp(String appId) {
        redisService.set("selectApp." + UserUtil.getUserId(),appId);
    }

    public Deployment deploymentFlow(String flowModelId) throws Exception {
        FlowModel flowModel=FlowModel.getById(flowModelId);

        if(StringUtils.isEmpty(flowModel.getContent())){
            throw new Exception("无模型内容");
        }

        Deployment deployment = repositoryService.createDeployment()
                .name(flowModel.getName())
                .tenantId(flowModel.getAppId())
                .addString(flowModel.getId()+".bpmn", flowModel.getContent())
                .deploy();

        flowModel.setDeploymentTime(deployment.getDeploymentTime());
        flowModel.save();

        return deployment;
    }

    public String save(FlowModelInputVO inputVO) throws Exception {
        String appId=appUserService.getSelectAppId();

        String processKey=null;
        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(IOUtils.toInputStream(inputVO.getContent(), StandardCharsets.UTF_8));
            Collection<Process> collections = bpmnModelInstance.getModelElementsByType(Process.class);
            if(collections.size()>0){
                processKey=collections.stream().toList().get(0).getId();
            }
        }

        return FlowModel.save(inputVO.getId(),appId,inputVO.getName(),inputVO.getContent(),processKey);
    }
}

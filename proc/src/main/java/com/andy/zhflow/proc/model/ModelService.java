package com.andy.zhflow.proc.model;

import com.andy.zhflow.proc.BpmnUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Component
public class ModelService {

    @Autowired
    private RepositoryService repositoryService;

    public Deployment deploymentFlow(String id) throws Exception {
        Model model = Model.getById(id);

        if(StringUtils.isEmpty(model.getContent())){
            throw new Exception("无模型内容");
        }

        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addString(model.getId()+".bpmn", model.getContent())
                .tenantId(model.getAppId())
                .deploy();

        model.setDeploymentTime(deployment.getDeploymentTime());
        model.save();

        return deployment;
    }

    public String save(ModelInputVO inputVO) throws Exception {

        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            String key=BpmnUtil.getProcKey(inputVO.getContent());
            inputVO.setProcKey(key);
        }

        return Model.save(inputVO);
    }
}

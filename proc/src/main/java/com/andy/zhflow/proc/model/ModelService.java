package com.andy.zhflow.proc.model;

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

    public Deployment deploymentFlow(String flowModelId) throws Exception {
        Model model = Model.getById(flowModelId);

        if(StringUtils.isEmpty(model.getContent())){
            throw new Exception("无模型内容");
        }

        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addString(model.getId()+".bpmn", model.getContent())
                .deploy();

        model.setDeploymentTime(deployment.getDeploymentTime());
        model.save();

        return deployment;
    }

    public String save(ModelInputVO inputVO) throws Exception {

        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(IOUtils.toInputStream(inputVO.getContent(), StandardCharsets.UTF_8));
            Collection<Process> collections = bpmnModelInstance.getModelElementsByType(Process.class);
            if(collections.size()>0) inputVO.setProcKey(collections.stream().toList().get(0).getId());
        }

        return Model.save(inputVO);
    }
}

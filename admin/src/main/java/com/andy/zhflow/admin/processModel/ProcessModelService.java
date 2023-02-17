package com.andy.zhflow.admin.processModel;

import com.andy.zhflow.processModel.ProcessModel;
import com.andy.zhflow.processModel.ProcessModelInputVO;
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
public class ProcessModelService {

    @Autowired
    private RepositoryService repositoryService;

    public Deployment deploymentFlow(String flowModelId) throws Exception {
        ProcessModel processModel = ProcessModel.getById(flowModelId);

        if(StringUtils.isEmpty(processModel.getContent())){
            throw new Exception("无模型内容");
        }

        Deployment deployment = repositoryService.createDeployment()
                .name(processModel.getName())
                .addString(processModel.getId()+".bpmn", processModel.getContent())
                .deploy();

        processModel.setDeploymentTime(deployment.getDeploymentTime());
        processModel.save();

        return deployment;
    }

    public String save(ProcessModelInputVO inputVO) throws Exception {

        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(IOUtils.toInputStream(inputVO.getContent(), StandardCharsets.UTF_8));
            Collection<Process> collections = bpmnModelInstance.getModelElementsByType(Process.class);
            if(collections.size()>0) inputVO.setProcessKey(collections.stream().toList().get(0).getId());
        }

        return ProcessModel.save(inputVO);
    }
}

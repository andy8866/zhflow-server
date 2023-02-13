package com.andy.zhflow.admin.processTemplate;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.processTemplate.ProcessTemplate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Component
public class ProcessTemplateService {

    @Autowired
    private AppUserService appUserService;

    public String save(ProcessTemplateInputVO inputVO) throws Exception {

        String processKey=null;
        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(IOUtils.toInputStream(inputVO.getContent(), StandardCharsets.UTF_8));
            Collection<Process> collections = bpmnModelInstance.getModelElementsByType(Process.class);
            if(collections.size()>0){
                processKey=collections.stream().toList().get(0).getId();
            }
        }

        return ProcessTemplate.save(inputVO.getId(),inputVO.getName(),inputVO.getContent(),processKey,inputVO.getCatalog());
    }
}

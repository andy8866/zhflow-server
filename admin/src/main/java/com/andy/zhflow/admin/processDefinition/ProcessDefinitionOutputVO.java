package com.andy.zhflow.admin.processDefinition;

import camundajar.impl.scala.Int;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcessDefinitionOutputVO {

    private String id;
    private String category;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;

    private boolean isSuspended;
    private String description;
    private String versionTag;

    public static ProcessDefinitionOutputVO convert(ProcessDefinition processDefinition){
        ProcessDefinitionOutputVO processDeploymentOutputVO =new ProcessDefinitionOutputVO();
        BeanUtils.copyProperties(processDefinition, processDeploymentOutputVO);

        return processDeploymentOutputVO;
    }

    public static List<ProcessDefinitionOutputVO> convertList(List<ProcessDefinition> list){
        List<ProcessDefinitionOutputVO> voList=new ArrayList<>();

        for (ProcessDefinition processDefinition:list){
            voList.add(convert(processDefinition));
        }

        return voList;
    }
}

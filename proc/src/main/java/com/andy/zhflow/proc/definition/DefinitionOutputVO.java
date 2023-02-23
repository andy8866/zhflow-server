package com.andy.zhflow.proc.definition;

import lombok.Data;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class DefinitionOutputVO {

    private String id;
    private String category;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;

    private boolean isSuspended;

    private String description;
    private String versionTag;

    public static DefinitionOutputVO convert(ProcessDefinition processDefinition){
        DefinitionOutputVO processDeploymentOutputVO =new DefinitionOutputVO();
        BeanUtils.copyProperties(processDefinition, processDeploymentOutputVO);

        return processDeploymentOutputVO;
    }

    public static List<DefinitionOutputVO> convertList(List<ProcessDefinition> list){
        List<DefinitionOutputVO> voList=new ArrayList<>();

        for (ProcessDefinition processDefinition:list){
            voList.add(convert(processDefinition));
        }

        return voList;
    }
}

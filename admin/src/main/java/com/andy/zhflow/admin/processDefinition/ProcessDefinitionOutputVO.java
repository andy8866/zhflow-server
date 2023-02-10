package com.andy.zhflow.admin.processDefinition;

import lombok.Data;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
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
    public boolean getIsSuspended(){
        return isSuspended;
    }
    public void setIsSuspended(Boolean b){
        isSuspended=b;
    }

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

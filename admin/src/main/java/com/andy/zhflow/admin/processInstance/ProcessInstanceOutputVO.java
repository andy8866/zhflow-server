package com.andy.zhflow.admin.processInstance;

import lombok.Data;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProcessInstanceOutputVO {

    private String id;

    private String processInstanceId;
    private String processDefinitionId;
    private String deploymentId;

    private String name;

    public static ProcessInstanceOutputVO convert(ProcessInstance processInstance){
        ProcessInstanceOutputVO processInstanceOutputVO =new ProcessInstanceOutputVO();
        BeanUtils.copyProperties(processInstance, processInstanceOutputVO);

        return processInstanceOutputVO;
    }

    public static List<ProcessInstanceOutputVO> convertList(List<ProcessInstance> list){
        List<ProcessInstanceOutputVO> voList=new ArrayList<>();

        for (ProcessInstance processInstance:list){
            voList.add(convert(processInstance));
        }

        return voList;
    }
}

package com.andy.zhflow.proc.instance;

import lombok.Data;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class InstanceOutputVO {

    private String id;

    private String processInstanceId;
    private String processDefinitionId;
    private String deploymentId;

    private String name;

    public static InstanceOutputVO convert(ProcessInstance processInstance){
        InstanceOutputVO instanceOutputVO =new InstanceOutputVO();
        BeanUtils.copyProperties(processInstance, instanceOutputVO);

        return instanceOutputVO;
    }

    public static List<InstanceOutputVO> convertList(List<ProcessInstance> list){
        List<InstanceOutputVO> voList=new ArrayList<>();

        for (ProcessInstance processInstance:list){
            voList.add(convert(processInstance));
        }

        return voList;
    }
}

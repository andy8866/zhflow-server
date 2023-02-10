package com.andy.zhflow.admin.processDeployment;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcessDeploymentOutputVO {

    private String id;
    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;

    public static ProcessDeploymentOutputVO convert(Deployment deployment){
        ProcessDeploymentOutputVO processDeploymentOutputVO =new ProcessDeploymentOutputVO();
        BeanUtils.copyProperties(deployment, processDeploymentOutputVO);

        return processDeploymentOutputVO;
    }

    public static List<ProcessDeploymentOutputVO> convertList(List<Deployment> list){
        List<ProcessDeploymentOutputVO> voList=new ArrayList<>();

        for (Deployment deployment:list){
            voList.add(convert(deployment));
        }

        return voList;
    }
}

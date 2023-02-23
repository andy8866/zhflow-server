package com.andy.zhflow.proc.deployment;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DeploymentOutputVO {

    private String id;
    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;

    public static DeploymentOutputVO convert(Deployment deployment){
        DeploymentOutputVO deploymentOutputVO =new DeploymentOutputVO();
        BeanUtils.copyProperties(deployment, deploymentOutputVO);

        return deploymentOutputVO;
    }

    public static List<DeploymentOutputVO> convertList(List<Deployment> list){
        List<DeploymentOutputVO> voList=new ArrayList<>();

        for (Deployment deployment:list){
            voList.add(convert(deployment));
        }

        return voList;
    }
}

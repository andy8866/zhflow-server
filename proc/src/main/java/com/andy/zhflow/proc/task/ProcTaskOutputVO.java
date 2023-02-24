package com.andy.zhflow.proc.task;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcTaskOutputVO {

    private String id;

    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String deploymentId;

    private String name;

    private String assignee;
    private String assigneeName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdated;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    private String formKey;

    public static ProcTaskOutputVO convert(Task task){
        ProcTaskOutputVO procTaskOutputVO =new ProcTaskOutputVO();
        BeanUtils.copyProperties(task, procTaskOutputVO);

        return procTaskOutputVO;
    }

    public static List<ProcTaskOutputVO> convertList(List<Task> list){
        List<ProcTaskOutputVO> voList=new ArrayList<>();

        for (Task task:list){
            voList.add(convert(task));
        }

        return voList;
    }

    public static ProcTaskOutputVO convert(HistoricTaskInstance task){
        ProcTaskOutputVO procTaskOutputVO =new ProcTaskOutputVO();
        BeanUtils.copyProperties(task, procTaskOutputVO);

        return procTaskOutputVO;
    }

    public static List<ProcTaskOutputVO> convertListFromHistory(List<HistoricTaskInstance> list){
        List<ProcTaskOutputVO> voList=new ArrayList<>();

        for (HistoricTaskInstance task:list){
            voList.add(convert(task));
        }

        return voList;
    }
}

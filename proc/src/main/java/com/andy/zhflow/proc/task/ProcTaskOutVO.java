package com.andy.zhflow.proc.task;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcTaskOutVO {

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

    public static ProcTaskOutVO convert(Task task){
        ProcTaskOutVO procTaskOutVO =new ProcTaskOutVO();
        BeanUtils.copyProperties(task, procTaskOutVO);

        return procTaskOutVO;
    }

    public static List<ProcTaskOutVO> convertList(List<Task> list){
        List<ProcTaskOutVO> voList=new ArrayList<>();

        for (Task task:list){
            voList.add(convert(task));
        }

        return voList;
    }

    public static ProcTaskOutVO convert(HistoricTaskInstance task){
        ProcTaskOutVO procTaskOutVO =new ProcTaskOutVO();
        BeanUtils.copyProperties(task, procTaskOutVO);

        return procTaskOutVO;
    }

    public static List<ProcTaskOutVO> convertListFromHistory(List<HistoricTaskInstance> list){
        List<ProcTaskOutVO> voList=new ArrayList<>();

        for (HistoricTaskInstance task:list){
            voList.add(convert(task));
        }

        return voList;
    }
}

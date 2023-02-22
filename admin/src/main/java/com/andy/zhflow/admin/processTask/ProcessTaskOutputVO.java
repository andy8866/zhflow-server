package com.andy.zhflow.admin.processTask;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcessTaskOutputVO {

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
    private Date dueDate;

    private String formKey;

    public static ProcessTaskOutputVO convert(Task task){
        ProcessTaskOutputVO processTaskOutputVO =new ProcessTaskOutputVO();
        BeanUtils.copyProperties(task, processTaskOutputVO);

        return processTaskOutputVO;
    }

    public static List<ProcessTaskOutputVO> convertList(List<Task> list){
        List<ProcessTaskOutputVO> voList=new ArrayList<>();

        for (Task task:list){
            voList.add(convert(task));
        }

        return voList;
    }
}

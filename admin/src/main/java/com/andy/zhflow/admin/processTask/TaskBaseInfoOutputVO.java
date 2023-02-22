package com.andy.zhflow.admin.processTask;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TaskBaseInfoOutputVO {

    private String taskId;
    private String startUser;
    private String startUserName;
}

package com.andy.zhflow.admin.processHistory;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcessHistoryOutputVO {

    private String id;

    private String processDefinitionKey;
    private String processDefinitionId;

    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Long durationInMillis;

    public static ProcessHistoryOutputVO convert(HistoricProcessInstance historicProcessInstance){
        ProcessHistoryOutputVO processHistoryOutputVO =new ProcessHistoryOutputVO();
        BeanUtils.copyProperties(historicProcessInstance, processHistoryOutputVO);

        return processHistoryOutputVO;
    }

    public static List<ProcessHistoryOutputVO> convertList(List<HistoricProcessInstance> list){
        List<ProcessHistoryOutputVO> voList=new ArrayList<>();

        for (HistoricProcessInstance historicProcessInstance:list){
            voList.add(convert(historicProcessInstance));
        }

        return voList;
    }
}

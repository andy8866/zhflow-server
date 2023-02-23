package com.andy.zhflow.proc.history;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcHistoryOutputVO {

    private String id;

    private String processDefinitionKey;
    private String processDefinitionId;

    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Long durationInMillis;

    public static ProcHistoryOutputVO convert(HistoricProcessInstance historicProcessInstance){
        ProcHistoryOutputVO procHistoryOutputVO =new ProcHistoryOutputVO();
        BeanUtils.copyProperties(historicProcessInstance, procHistoryOutputVO);

        return procHistoryOutputVO;
    }

    public static List<ProcHistoryOutputVO> convertList(List<HistoricProcessInstance> list){
        List<ProcHistoryOutputVO> voList=new ArrayList<>();

        for (HistoricProcessInstance historicProcessInstance:list){
            voList.add(convert(historicProcessInstance));
        }

        return voList;
    }
}

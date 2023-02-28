package com.andy.zhflow.proc.history;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProcHistoryProcOutVO {

    private String id;

    private String processDefinitionKey;
    private String processDefinitionId;

    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Long durationInMillis;

    private String state;

    private String deleteReason;

    public static ProcHistoryProcOutVO convert(HistoricProcessInstance historicProcessInstance){
        ProcHistoryProcOutVO procHistoryProcOutVO =new ProcHistoryProcOutVO();
        BeanUtils.copyProperties(historicProcessInstance, procHistoryProcOutVO);

        return procHistoryProcOutVO;
    }

    public static List<ProcHistoryProcOutVO> convertList(List<HistoricProcessInstance> list){
        List<ProcHistoryProcOutVO> voList=new ArrayList<>();

        for (HistoricProcessInstance historicProcessInstance:list){
            voList.add(convert(historicProcessInstance));
        }

        return voList;
    }
}

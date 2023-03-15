package com.scyingneng.zhflow.proc.copy;

import com.scyingneng.zhflow.proc.BpmnConstant;
import com.scyingneng.zhflow.service.security.IAuthService;
import com.scyingneng.zhflow.service.thirdApp.IThirdAppService;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CopyService {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IThirdAppService thirdAppService;

    @Autowired
    private IAuthService authService;

    public void makeCopy(String taskId, Map<String,Object> inputVO) throws Exception {
        String userIds = (String) inputVO.getOrDefault(BpmnConstant.VAR_COPY_USER_IDS, null);
        makeCopy(taskId,userIds);
    }

    public void makeCopy(String taskId,String userIds) throws Exception {
        if(StringUtils.isEmpty(userIds)) return ;

        String[] userIdList = userIds.split(",");
        if(userIdList.length==0) return;

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();

        String title= historicProcessInstance.getProcessDefinitionName() + "-" + historicTaskInstance.getName();

        for (String userId : userIdList) {
            Copy copy=new Copy();
            copy.setAppId(historicTaskInstance.getTenantId());

            copy.setTitle(title);

            copy.setTaskId(historicTaskInstance.getId());
            copy.setTaskName(historicTaskInstance.getName());

            copy.setProcInsId(historicProcessInstance.getId());
            copy.setProcName(historicProcessInstance.getProcessDefinitionName());

            copy.setUserId(userId);
            copy.setUserName(thirdAppService.getUserNameById(historicTaskInstance.getTenantId(),userId));

            copy.setOriginatorId(authService.getUserId());
            copy.setOriginatorName(thirdAppService.getUserNameById(historicTaskInstance.getTenantId(), authService.getUserId()));

            copy.save();
        }
    }
}

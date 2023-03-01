package com.andy.zhflow.proc.copy;

import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.service.user.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
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
    private RepositoryService repositoryService;

    @Autowired
    private IUserService userService;

    public void makeCopy(String taskId, Map<String,Object> inputVO){
        String userIds = (String) inputVO.getOrDefault(BpmnConstant.VAR_COPY_USER_IDS, null);
        makeCopy(taskId,userIds);
    }

    public void makeCopy(String taskId,String userIds){
        if(StringUtils.isEmpty(userIds)) return ;

        String[] userIdList = userIds.split(",");
        if(userIdList.length==0) return;

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();

        String title= historicProcessInstance.getProcessDefinitionName() + "-" + historicTaskInstance.getName();

        for (String userId : userIdList) {
            Copy copy=new Copy();
            copy.setTitle(title);

            copy.setTaskId(historicTaskInstance.getId());
            copy.setTaskName(historicTaskInstance.getName());

            copy.setProcInsId(historicProcessInstance.getId());
            copy.setProcName(historicProcessInstance.getProcessDefinitionName());

            copy.setUserId(userId);
            copy.setUserName(userService.getNameById(userId));

            copy.setOriginatorId(UserUtil.getUserId());
            copy.setOriginatorName(userService.getNameById(UserUtil.getUserId()));

            copy.save();
        }
    }
}

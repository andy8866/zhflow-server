package com.andy.zhflow.proc;

import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.user.UserService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcListener {

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected IUserService userService;

    public void setSuperiorUser(DelegateTask task){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().desc()
                .list();

        String userid=list.get(0).getAssignee();
        String superiorUserId=userService.getSuperiorUserId(userid);
        task.setAssignee(superiorUserId);
    }
}

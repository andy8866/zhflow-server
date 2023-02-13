package com.andy.zhflow.admin.processTask;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcessTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppUserService appUserService;

    public AmisPage<ProcessTaskOutputVO> getAgendaTaskList(Integer page, Integer perPage,String userId) {
        String appId=appUserService.getSelectAppId();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .tenantIdIn(appId)
                .taskAssignee(userId)
                ;

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcessTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public AmisPage<ProcessTaskOutputVO> getClaimList(Integer page, Integer perPage,String userId) {
        String appId=appUserService.getSelectAppId();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .tenantIdIn(appId)
                .withoutCandidateUsers()
               ;

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcessTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public void claim(String taskId) {
        taskService.claim(taskId,UserUtil.getUserId());
    }

    private List<ProcessTaskOutputVO> convertList(List<Task> list){
        List<ProcessTaskOutputVO> outList = ProcessTaskOutputVO.convertList(list);
        for (ProcessTaskOutputVO outputVO:outList){
            if(StringUtils.isNoneEmpty(outputVO.getOwner())){
                outputVO.setOwnerName(User.getNameById(outputVO.getOwner()));
            }

            if(StringUtils.isNoneEmpty(outputVO.getAssignee())){
                outputVO.setAssigneeName(User.getNameById(outputVO.getAssignee()));
            }
        }
        return outList;
    }

    public void doTask(DoTaskInputVO inputVO) throws Exception {

        Task task = taskService.createTaskQuery().taskId(inputVO.getTaskId()).singleResult();

        taskService.createComment(task.getId(),task.getProcessInstanceId(), inputVO.getOpinion());

        Map<String, Object> variables = new HashMap<>(BeanUtils.describe(inputVO));
        taskService.complete(inputVO.getTaskId(),variables);
    }

    public void assignee(String taskId, String assigneeUserId) {
        taskService.setAssignee(taskId,assigneeUserId);
    }
}

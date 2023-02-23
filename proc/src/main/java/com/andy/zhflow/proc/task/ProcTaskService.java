package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.ApprovalProcDiagramOutputItemVO;
import com.andy.zhflow.proc.doProc.DoProcService;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcTaskService {

    @Resource
    private org.camunda.bpm.engine.TaskService taskService;
    @Resource
    private FormService formService;

    @Resource
    private RuntimeService runtimeService;

    @Autowired
    private DoProcService doProcService;

    @Autowired
    protected HistoryService historyService;

    /**
     * 待办任务
     * @param page
     * @param perPage
     * @param userId
     * @return
     */
    public AmisPage<ProcTaskOutputVO> getAgendaTaskList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userId)
                .initializeFormKeys()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public AmisPage<ProcTaskOutputVO> getClaimList(Integer page, Integer perPage, String userId) {

        TaskQuery taskQuery = taskService.createTaskQuery().withoutCandidateUsers()
                .orderByTaskCreateTime().desc();

        Long total=taskQuery.count();

        List<Task> list = taskQuery.listPage((page-1) * perPage, perPage);

        List<ProcTaskOutputVO> outList =convertList(list);

        return AmisPage.transitionPage(outList,total);
    }

    public void claim(String taskId) {
        taskService.claim(taskId,UserUtil.getUserId());
    }

    private List<ProcTaskOutputVO> convertList(List<Task> list){
        List<ProcTaskOutputVO> outList = ProcTaskOutputVO.convertList(list);

        for (ProcTaskOutputVO outputVO:outList){
            if(StringUtils.isNoneEmpty(outputVO.getAssignee()))  outputVO.setAssigneeName(User.getNameById(outputVO.getAssignee()));
        }
        return outList;
    }

    public void completeTask(String taskId,Map<String,Object> inputVO) throws Exception {
        taskService.complete(taskId,inputVO);
    }

    public void assignee(String taskId, String assigneeUserId) {
        taskService.setAssignee(taskId,assigneeUserId);
    }

    public void getTaskUi(String taskId) {
    }

    public Map<String,Object> getProcessVar(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
        variables.remove("taskId");
        return variables;


    }

    public Map<String,Object> getTaskLastVar(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .finished().orderByHistoricTaskInstanceEndTime().desc()
                .list();

        if(list.size()==0) return new HashMap<>();

        HistoricTaskInstance last=list.get(0);
        List<HistoricDetail> historicDetailList = historyService.createHistoricDetailQuery()
                .processInstanceId(task.getProcessInstanceId())
                .activityInstanceId(last.getActivityInstanceId()).list();
        return doProcService.historicDetailVarToMap(historicDetailList);
    }

    public List<ApprovalProcDiagramOutputItemVO> getApprovalProcessDiagramData(String taskId) throws Exception {
        DoProcService processServer = doProcService.getProcessServer(taskId);
        return processServer.getApprovalProcessDiagramData(taskId);
    }
}

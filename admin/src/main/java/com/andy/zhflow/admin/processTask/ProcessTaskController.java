package com.andy.zhflow.admin.processTask;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/admin/processTask")
public class ProcessTaskController {

    @Autowired
    private ProcessTaskService processTaskService;


    @GetMapping(value="/getAgendaTaskList")
    public ResultResponse<AmisPage<ProcessTaskOutputVO>> getAgendaTaskList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessTaskOutputVO> appPage = processTaskService.getAgendaTaskList(page, perPage,
                UserUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/getClaimList")
    public ResultResponse<AmisPage<ProcessTaskOutputVO>> getClaimList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessTaskOutputVO> appPage = processTaskService.getClaimList(page, perPage,
                UserUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/claim")
    public ResultResponse<Void> claim(@RequestParam("taskId") String taskId) throws Exception {
        processTaskService.claim(taskId);
        return ResultResponse.success();
    }

    @GetMapping(value="/getTaskUi")
    public ResultResponse<Void> getTaskUi(@RequestParam("taskId") String taskId) throws Exception {
        processTaskService.getTaskUi(taskId);
        return ResultResponse.success();
    }

    @PostMapping(value="/completeTask")
    public ResultResponse<Void> completeTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        processTaskService.completeTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @GetMapping(value="/assignee")
    public ResultResponse<Void> assignee(@RequestParam("taskId") String taskId,
                                         @RequestParam("assigneeUserId") String assigneeUserId) throws Exception {
        processTaskService.assignee(taskId,assigneeUserId);
        return ResultResponse.success();
    }

    @GetMapping(value="/getProcessVar")
    public ResultResponse<Map<String,Object>> getProcessVar(@RequestParam("taskId") String taskId) throws Exception {
        Map<String,Object> map=processTaskService.getProcessVar(taskId);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getTaskLastVar")
    public ResultResponse<Map<String,Object>> getTaskLastVar(@RequestParam("taskId") String taskId) throws Exception {
        Map<String,Object> map=processTaskService.getTaskLastVar(taskId);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getApprovalProcessDiagramData")
    public ResultResponse<List<ApprovalProcessDiagramOutputItemVO>> getApprovalProcessDiagramData(
            @RequestParam("taskId") String taskId) throws Exception {
        List<ApprovalProcessDiagramOutputItemVO> list=processTaskService.getApprovalProcessDiagramData(taskId);
        return ResultResponse.success(list);
    }
}

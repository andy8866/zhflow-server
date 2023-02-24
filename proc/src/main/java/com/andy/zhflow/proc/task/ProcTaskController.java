package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.ApprovalProcDiagramOutputItemVO;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/proc/task")
public class ProcTaskController {

    @Autowired
    private ProcTaskService procTaskService;


    @GetMapping(value="/getAgendaTaskList")
    public ResultResponse<AmisPage<ProcTaskOutputVO>> getAgendaTaskList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcTaskOutputVO> appPage = procTaskService.getAgendaTaskList(page, perPage,
                UserUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/getClaimList")
    public ResultResponse<AmisPage<ProcTaskOutputVO>> getClaimList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcTaskOutputVO> appPage = procTaskService.getClaimList(page, perPage,
                UserUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/claim")
    public ResultResponse<Void> claim(@RequestParam("taskId") String taskId) throws Exception {
        procTaskService.claim(taskId);
        return ResultResponse.success();
    }

    @GetMapping(value="/getTaskUi")
    public ResultResponse<Void> getTaskUi(@RequestParam("taskId") String taskId) throws Exception {
        procTaskService.getTaskUi(taskId);
        return ResultResponse.success();
    }

    @PostMapping(value="/completeTask")
    public ResultResponse<Void> completeTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.completeTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @GetMapping(value="/assignee")
    public ResultResponse<Void> assignee(@RequestParam("taskId") String taskId,
                                         @RequestParam("assigneeUserId") String assigneeUserId) throws Exception {
        procTaskService.assignee(taskId,assigneeUserId);
        return ResultResponse.success();
    }

    @GetMapping(value="/getProcVar")
    public ResultResponse<Map<String,Object>> getProcVar(@RequestParam("taskId") String taskId) throws Exception {
        Map<String,Object> map= procTaskService.getProcVar(taskId);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getTaskLastVar")
    public ResultResponse<Map<String,Object>> getTaskLastVar(@RequestParam("taskId") String taskId) throws Exception {
        Map<String,Object> map= procTaskService.getTaskLastVar(taskId);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getApprovalProcDiagramData")
    public ResultResponse<List<ApprovalProcDiagramOutputItemVO>> getApprovalProcDiagramData(
            @RequestParam("taskId") String taskId) throws Exception {
        List<ApprovalProcDiagramOutputItemVO> list= procTaskService.getApprovalProcDiagramData(taskId);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/getHistoryTask")
    public ResultResponse<List<String>> getHistoryTask(
            @RequestParam("taskId") String taskId) throws Exception {
        List<String> list=new ArrayList<>();
        list.add("NUVh1nOug36Rn4nm7-iYD");
        list.add("NUVh1nOug36Rn4nm7-iYD");
        return ResultResponse.success(list);
    }
}

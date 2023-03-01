package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value="/completeTask")
    public ResultResponse<Void> completeTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.completeTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/passTask")
    public ResultResponse<Void> passTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.passTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/rejectTask")
    public ResultResponse<Void> rejectTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.rejectTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/delegateTask")
    public ResultResponse<Void> delegateTask(@RequestParam("taskId") String taskId,
                                           @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.delegateTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/transferTask")
    public ResultResponse<Void> transferTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.transferTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/rebackTask")
    public ResultResponse<Void> rebackTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.rebackTask(taskId,inputVO);
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
        Map<String,Object> map= procTaskService.getProcVarByTaskId(taskId);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getTaskLastVar")
    public ResultResponse<Map<String,Object>> getTaskLastVar(@RequestParam("taskId") String taskId,
                                                             @RequestParam(value = "onlyTask",required = false) Boolean onlyTask) throws Exception {

        if(onlyTask==null) onlyTask=true;

        Map<String,Object> map= procTaskService.getTaskLastVar(taskId,onlyTask);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getTaskLastVarByTaskDefinitionKey")
    public ResultResponse<Map<String,Object>> getTaskLastVarByTaskDefinitionKey(@RequestParam("taskId") String taskId,
                                                             @RequestParam("taskDefinitionKey") String taskDefinitionKey
                                                             ) throws Exception {
        Map<String,Object> map= procTaskService.getTaskLastVarByTaskDefinitionKey(taskId,taskDefinitionKey);
        return ResultResponse.success(map);
    }

    @GetMapping(value="/getHistoryCompleteTask")
    public ResultResponse<List<ProcTaskOutputVO>> getHistoryCompleteTask(){
        List<ProcTaskOutputVO> list=procTaskService.getHistoryCompleteTask(UserUtil.getUserId());
        return ResultResponse.success(list);
    }
}

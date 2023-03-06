package com.andy.zhflow.proc.task;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.proc.doProc.ProcUserTaskService;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping(value = "/api/proc/userTask")
public class ProcUserTaskController {

    @Autowired
    private ProcUserTaskService userTaskService;

    @GetMapping(value="/getAgendaList")
    public ResultResponse<AmisPage<ProcTaskOutVO>> getAgendaList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcTaskOutVO> appPage = userTaskService.getAgendaList(page, perPage, AuthUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/getClaimList")
    public ResultResponse<AmisPage<ProcTaskOutVO>> getClaimList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcTaskOutVO> appPage = userTaskService.getClaimList(page, perPage, AuthUtil.getUserId());
        return ResultResponse.success(appPage);
    }

    @GetMapping(value="/claim")
    public ResultResponse<Void> claim(@RequestParam("taskId") String taskId) throws Exception {
        userTaskService.claim(taskId);
        return ResultResponse.success();
    }

    @PostMapping(value="/pass")
    public ResultResponse<Void> pass(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        userTaskService.pass(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/reject")
    public ResultResponse<Void> reject(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        userTaskService.reject(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/delegate")
    public ResultResponse<Void> delegate(@RequestParam("taskId") String taskId,
                                           @RequestBody() Map<String,Object> inputVO) throws Exception {
        userTaskService.delegate(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/transfer")
    public ResultResponse<Void> transfer(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        userTaskService.transfer(taskId,inputVO);
        return ResultResponse.success();
    }

    @PostMapping(value="/reback")
    public ResultResponse<Void> reback(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        userTaskService.reback(taskId,inputVO);
        return ResultResponse.success();
    }

    @GetMapping(value="/assignee")
    public ResultResponse<Void> assignee(@RequestParam("taskId") String taskId,
                                         @RequestParam("assigneeUserId") String assigneeUserId) throws Exception {
        userTaskService.assignee(taskId,assigneeUserId);
        return ResultResponse.success();
    }
}

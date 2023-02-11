package com.andy.zhflow.admin.processTask;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value="/doTask")
    public ResultResponse<Void> doTask(@RequestBody() DoTaskInputVO inputVO) throws Exception {
        processTaskService.doTask(inputVO);
        return ResultResponse.success();
    }
}

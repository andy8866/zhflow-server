package com.andy.zhflow.proc.task;

import com.andy.zhflow.proc.doProc.ProcTaskService;
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

    @PostMapping(value="/completeTask")
    public ResultResponse<Void> completeTask(@RequestParam("taskId") String taskId,
                                             @RequestBody() Map<String,Object> inputVO) throws Exception {
        procTaskService.completeTask(taskId,inputVO);
        return ResultResponse.success();
    }

    @GetMapping(value="/getHistoryCompleteTask")
    public ResultResponse<List<ProcTaskOutVO>> getHistoryCompleteTask(){
        List<ProcTaskOutVO> list=procTaskService.getHistoryCompleteTask(UserUtil.getUserId());
        return ResultResponse.success(list);
    }
}

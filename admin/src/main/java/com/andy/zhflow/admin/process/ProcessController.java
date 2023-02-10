package com.andy.zhflow.admin.process;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping(value="/startProcess")
    public ResultResponse<Void> startProcess(@RequestParam("processKey") String processKey) throws Exception {
        processService.startProcess(processKey);
        return ResultResponse.success();
    }
}

package com.andy.zhflow.admin.processInstance;

import com.andy.zhflow.admin.processDefinition.ProcessDefinitionOutputVO;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/processInstance")
public class ProcessInstanceController {

    @Autowired
    private ProcessInstanceService processInstanceService;

    @GetMapping(value="/startProcess")
    public ResultResponse<Void> startProcess(@RequestParam("processKey") String processKey) throws Exception {
        processInstanceService.startProcess(processKey);
        return ResultResponse.success();
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessInstanceOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessInstanceOutputVO> appPage = processInstanceService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

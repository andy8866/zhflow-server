package com.andy.zhflow.proc.instance;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/proc/instance")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @GetMapping(value="/startProcess")
    public ResultResponse<Void> startProcess(@RequestParam("processKey") String processKey) throws Exception {
        instanceService.startProcess(processKey);
        return ResultResponse.success();
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<InstanceOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<InstanceOutputVO> appPage = instanceService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

package com.andy.zhflow.proc.instance;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping(value = "/api/proc/instance")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @PostMapping(value="/startProcess")
    public ResultResponse<Void> startProcess(@RequestParam("processKey") String processKey,
                                             @RequestBody(required = false) Map<String,Object> vars) throws Exception {
        instanceService.startProcess(processKey,vars);
        return ResultResponse.success();
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<InstanceOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<InstanceOutputVO> appPage = instanceService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

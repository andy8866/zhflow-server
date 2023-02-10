package com.andy.zhflow.admin.processDeployment;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/processDeployment")
public class ProcessDeploymentController {

    @Autowired
    private ProcessDeploymentService processDeploymentService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessDeploymentOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessDeploymentOutputVO> appPage = processDeploymentService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

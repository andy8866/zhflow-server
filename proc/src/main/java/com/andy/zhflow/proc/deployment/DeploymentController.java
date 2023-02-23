package com.andy.zhflow.proc.deployment;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/proc/deployment")
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<DeploymentOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<DeploymentOutputVO> appPage = deploymentService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

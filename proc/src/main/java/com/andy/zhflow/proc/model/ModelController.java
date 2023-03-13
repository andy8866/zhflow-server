package com.andy.zhflow.proc.model;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.AuthService;
import com.andy.zhflow.service.security.IAuthService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/proc/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Autowired
    private IAuthService authService;


    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() ModelInputVO inputVO) throws Exception {
        String id= modelService.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<Model>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {

        String appId= authService.getAppId();
        IPage<Model> item = Model.selectPage(page, perPage,appId);

        return ResultResponse.success(AmisPage.transitionPage(item));
    }

    @GetMapping(value="/getById")
    public ResultResponse<Model> getById(@RequestParam("id") String id) {
        Model model = Model.getById(id);
        return ResultResponse.success(model);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        Model.del(id);
        return ResultResponse.success();
    }

    @GetMapping(value="/deploymentFlow")
    public ResultResponse<String> deploymentFlow(@RequestParam("id") String id) throws Exception {
        Deployment deployment = modelService.deploymentFlow(id);
        return ResultResponse.success(deployment.getId());
    }
}

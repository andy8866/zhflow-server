package com.andy.zhflow.admin.processModel;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.processModel.ProcessModel;
import com.andy.zhflow.processModel.ProcessModelInputVO;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/processModel")
public class ProcessModelController {

    @Autowired
    private ProcessModelService processModelService;


    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() ProcessModelInputVO inputVO) throws Exception {
        String id= processModelService.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessModel>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage,
            @RequestParam(value = "asTemplate",required = false) Boolean asTemplate) {

        IPage<ProcessModel> item = ProcessModel.selectPage(page, perPage, asTemplate);

        return ResultResponse.success(AmisPage.transitionPage(item));
    }

    @GetMapping(value="/getById")
    public ResultResponse<ProcessModel> getById(@RequestParam("id") String id) {
        ProcessModel processModel = ProcessModel.getById(id);
        return ResultResponse.success(processModel);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        ProcessModel.del(id);
        return ResultResponse.success();
    }

    @GetMapping(value="/deploymentFlow")
    public ResultResponse<String> deploymentFlow(@RequestParam("id") String id) throws Exception {
        Deployment deployment = processModelService.deploymentFlow(id);
        return ResultResponse.success(deployment.getId());
    }
}

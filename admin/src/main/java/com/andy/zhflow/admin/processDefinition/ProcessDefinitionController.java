package com.andy.zhflow.admin.processDefinition;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.processModel.ProcessModel;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessDefinitionOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessDefinitionOutputVO> appPage = processDefinitionService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

package com.andy.zhflow.admin.processTemplate;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.processTemplate.ProcessTemplate;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/processTemplate")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() ProcessTemplateInputVO inputVO) throws Exception {
        String id= processTemplateService.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessTemplate>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<ProcessTemplate> appPage = ProcessTemplate.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/getById")
    public ResultResponse<ProcessTemplate> getById(@RequestParam("id") String id) {
        ProcessTemplate processTemplate = ProcessTemplate.getById(id);
        return ResultResponse.success(processTemplate);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        ProcessTemplate.del(id);
        return ResultResponse.success();
    }
}

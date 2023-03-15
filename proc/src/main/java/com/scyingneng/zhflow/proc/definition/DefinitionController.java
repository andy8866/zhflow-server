package com.scyingneng.zhflow.proc.definition;

import com.scyingneng.zhflow.amis.AmisPage;
import com.scyingneng.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/proc/definition")
public class DefinitionController {

    @Autowired
    private DefinitionService definitionService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<DefinitionOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<DefinitionOutputVO> appPage = definitionService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

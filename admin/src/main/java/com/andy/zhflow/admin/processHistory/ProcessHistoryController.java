package com.andy.zhflow.admin.processHistory;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/processHistory")
public class ProcessHistoryController {

    @Autowired
    private ProcessHistoryService processHistoryService;
    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcessHistoryOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcessHistoryOutputVO> appPage = processHistoryService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

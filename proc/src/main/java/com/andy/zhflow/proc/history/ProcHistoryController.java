package com.andy.zhflow.proc.history;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/proc/history")
public class ProcHistoryController {

    @Autowired
    private ProcHistoryService procHistoryService;
    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<ProcHistoryOutputVO>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcHistoryOutputVO> appPage = procHistoryService.getList(page, perPage);
        return ResultResponse.success(appPage);
    }
}

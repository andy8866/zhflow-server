package com.andy.zhflow.proc.history;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/proc/history")
public class ProcHistoryController {

    @Autowired
    private ProcHistoryService procHistoryService;
    @GetMapping(value="/getProcList")
    public ResultResponse<AmisPage<ProcHistoryProcOutVO>> getProcList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcHistoryProcOutVO> appPage = procHistoryService.getList(page, perPage,UserUtil.getUserId());
        return ResultResponse.success(appPage);
    }
}

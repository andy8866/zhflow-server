package com.scyingneng.zhflow.proc.history;

import com.scyingneng.zhflow.amis.AmisPage;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.service.security.IAuthService;
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

    @Autowired
    private IAuthService authService;

    @GetMapping(value="/getProcList")
    public ResultResponse<AmisPage<ProcHistoryProcOutVO>> getProcList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        AmisPage<ProcHistoryProcOutVO> appPage = procHistoryService.getList(page, perPage, authService.getUserId());
        return ResultResponse.success(appPage);
    }

}

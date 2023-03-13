package com.andy.zhflow.third.app;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.third.appConfig.AppConfig;
import com.andy.zhflow.third.token.TokenApiService;
import com.andy.zhflow.uiPage.UiPage;
import com.andy.zhflow.uiPage.UiPageInputVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/app")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping(value="/getList")
    public ResultResponse<List<App>> getList() throws Exception {
        List<App> list=App.getList();
        return ResultResponse.success(list);
    }

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() AppInputVO inputVO) throws Exception {
        String id= App.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getNanoId")
    public ResultResponse<String> getNanoId() throws Exception {
        return ResultResponse.success(NanoIdUtils.randomNanoId());
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) throws Exception {
        App.del(id);
        return ResultResponse.success();
    }

    @PostMapping(value="/copyConfig")
    public ResultResponse<Void> copyConfig(@RequestBody AppConfigCopyInputVO appConfigCopyInputVO) throws Exception {
        appService.copyConfig(appConfigCopyInputVO);
        return ResultResponse.success();
    }
}

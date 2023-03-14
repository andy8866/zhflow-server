package com.andy.zhflow.third.app;

import com.andy.zhflow.response.ResultResponse;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value="/switchApp")
    public ResultResponse<String> switchApp(@RequestParam("id") String appId) throws Exception {
        if(StringUtils.isEmpty(appId)) appId=appService.getFirstAppId();

        String appToken=appService.switchApp(appId,null);
        return ResultResponse.success(appToken);
    }
}

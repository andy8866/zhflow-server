package com.scyingneng.zhflow.third.appConfig;

import com.scyingneng.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/appConfig")
public class AppConfigController {

    @GetMapping(value="/getList")
    public ResultResponse<List<AppConfig>> getHttpRequestList(
            @RequestParam("appId") String appId,@RequestParam("type") String type) throws Exception {
        List<AppConfig> list=AppConfig.getList(appId,type);
        return ResultResponse.success(list);
    }

    @PostMapping(value="/save")
    public ResultResponse<String> saveHttpRequest(@RequestBody() AppConfigInputVO inputVO) throws Exception {
        String id=AppConfig.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) throws Exception {
        AppConfig.delById(id);
        return ResultResponse.success();
    }


}

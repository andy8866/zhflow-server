package com.andy.zhflow.admin.flowModel;

import com.andy.zhflow.admin.appuser.AppUserService;
import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.flowModel.FlowModel;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/flowModel")
public class FlowModelController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() FlowModelInputVO inputVO) throws Exception {
        String appId=appUserService.getSelectAppId();
        String id= FlowModel.save(inputVO.getId(),appId,inputVO.getName(),inputVO.getContent());
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<FlowModel>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        String appId=appUserService.getSelectAppId();
        IPage<FlowModel> appPage = FlowModel.selectPage(page, perPage,appId);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/getById")
    public ResultResponse<FlowModel> getById(@RequestParam("id") String id) {
        FlowModel flowModel = FlowModel.getById(id);
        return ResultResponse.success(flowModel);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        FlowModel.del(id);
        return ResultResponse.success();
    }
}

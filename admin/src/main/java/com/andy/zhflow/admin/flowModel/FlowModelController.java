package com.andy.zhflow.admin.flowModel;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.flowModel.FlowModel;
import com.andy.zhflow.response.ResultResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/flowModel")
public class FlowModelController {

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() FlowModelInputVO inputVO) throws Exception {
        String id= FlowModel.save(inputVO.getId(),inputVO.getAppId(),inputVO.getName(),inputVO.getContent());
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<FlowModel>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage,
        @RequestParam("appId") String appId) {
        IPage<FlowModel> appPage = FlowModel.selectPage(page, perPage,appId);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/getById")
    public ResultResponse<FlowModel> getById(@RequestParam("id") String id) {
        FlowModel flowModel = FlowModel.getById(id);
        return ResultResponse.success(flowModel);
    }

    @GetMapping(value="/delById")
    public ResultResponse<Void> delById(@RequestParam("id") String id) {
        FlowModel.del(id);
        return ResultResponse.success();
    }
}

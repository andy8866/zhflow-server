package com.andy.zhflow.admin.app;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.app.App;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.suggest.Suggest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/admin/app")
public class AppController {

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() AppInputVO appInputVO) throws Exception {
        String id=App.save(appInputVO.getId(),appInputVO.getName());
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<App>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<App> appPage = App.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        App.del(id);
        return ResultResponse.success(null);
    }
}

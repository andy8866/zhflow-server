package com.andy.zhflow.admin.suggest;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.suggest.Suggest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping(value = "/api/admin/suggest")
public class SuggestController {

    /**
     * 建议
     * @return
     */
    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<Suggest>> getList(@RequestParam("page") Integer page,@RequestParam("perPage") Integer perPage) {
        IPage<Suggest> suggestPage = Suggest.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(suggestPage));
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        Suggest.del(id);
        return ResultResponse.success(null);
    }
}

package com.scyingneng.zhflow.admin.suggest;

import com.scyingneng.zhflow.amis.AmisPage;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.suggest.Suggest;
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

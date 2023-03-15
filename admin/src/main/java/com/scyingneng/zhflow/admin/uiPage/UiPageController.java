package com.scyingneng.zhflow.admin.uiPage;

import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.uiPage.UiPage;
import com.scyingneng.zhflow.uiPage.UiPageInputVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping(value = "/api/admin/uiPage")
public class UiPageController {

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() UiPageInputVO inputVO) throws Exception {
        String id= UiPage.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getById")
    public ResultResponse<UiPage> getById(@RequestParam("id") String id) {
        UiPage page = UiPage.getById(id);
        return ResultResponse.success(page);
    }

    @GetMapping(value="/getList")
    public ResultResponse<List<UiPage>> getList() {
        List<UiPage> list = UiPage.getList();
        return ResultResponse.success(list);
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        UiPage.del(id);
        return ResultResponse.success();
    }

    @GetMapping(value="/getUiByCode")
    public void getUiByCode(HttpServletResponse response,@RequestParam("code") String code) {
        String ui = UiPage.getUiByCode(code);
        ResponseUtil.writeString(response,ui);
    }
}

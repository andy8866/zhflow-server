package com.scyingneng.zhflow.admin.theme;

import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.theme.Theme;
import com.scyingneng.zhflow.theme.ThemeInputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController()
@RequestMapping(value = "/api/admin/theme")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @GetMapping(value="/getList")
    public ResultResponse<List<Theme>> getList() {
        List<Theme>  list=Theme.getList();

        return ResultResponse.success(list);
    }


    @PostMapping(value="/save")
    public ResultResponse<Void> save(@RequestBody ThemeInputVO inputVO) {
        Theme.save(inputVO);
        return ResultResponse.success();
    }

    @GetMapping(value="/getEdit")
    public ResultResponse<Map<String,Object>> getEdit(@RequestParam("id") String id) {
        Map<String,Object> map=Theme.getEdit(id);
        return ResultResponse.success(map);
    }

    @PostMapping(value="/saveEdit")
    public ResultResponse<Void> saveEdit(@RequestParam("id") String id,@RequestBody Map<String,Object> map) {
        Theme.saveEdit(id,map);

        return ResultResponse.success();
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) {
        Theme.del(id);
        return ResultResponse.success(null);
    }

    @GetMapping(value="/use")
    public ResultResponse<Void> use(@RequestParam(value = "id",required = false) String id) {
        Theme.use(id);
        return ResultResponse.success(null);
    }

    @GetMapping(value="/getCss")
    public void getCss(HttpServletResponse response,@RequestParam("id") String id) {
        themeService.getCss(response,id);
    }

    @GetMapping(value="/getUseCss")
    public void getUseCss(HttpServletResponse response) {
        themeService.getUseCss(response);
    }
}

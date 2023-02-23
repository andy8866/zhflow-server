package com.andy.zhflow.proc.ui;

import com.andy.zhflow.response.ResponseUtil;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping(value = "/api/proc/ui")
public class UiController {

    @Autowired
    private UiService uiService;

    @GetMapping(value="/getList")
    public ResultResponse<List<Ui>> getList(@RequestParam(value = "name",required = false) String name,
                                            @RequestParam(value = "type",required = false) String type,
                                            @RequestParam(value = "noType",required = false) String noType) throws Exception {
        List<Ui> list = Ui.getList(name,type,noType);
        return ResultResponse.success(list);
    }

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() UiInputVO inputVO) throws Exception {
        String id= Ui.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getById")
    public ResultResponse<Ui> getById(@RequestParam("id") String id) throws Exception {
        Ui ui = Ui.getById(id);
        return ResultResponse.success(ui);
    }

    @GetMapping(value="/getContentById")
    public void getContentById(HttpServletResponse response,
                                   @RequestParam("id") String id) throws Exception {
        ResponseUtil.writeString(response,Ui.getById(id).getContent());
    }

    @GetMapping(value="/getContentByTaskId")
    public void getContentByTaskId(HttpServletResponse response,
                               @RequestParam("taskId") String taskId) throws Exception {
        String content= uiService.getContentByTaskId(taskId);
        ResponseUtil.writeString(response,content);
    }

    @GetMapping(value="/getContentByCode")
    public void getContentByCode(HttpServletResponse response,
                                   @RequestParam("code") String code) throws Exception {
        ResponseUtil.writeString(response, Ui.getByCode(code).getContent());
    }
}

package com.scyingneng.zhflow.proc.ui;

import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping(value = "/api/proc/ui")
public class UiController {

    @Autowired
    private UiService uiService;

    @GetMapping(value="/getCompList")
    public ResultResponse<List<Ui>> getCompList(@RequestParam(value = "name",required = false) String name) throws Exception {
        List<Ui> list = Ui.getCompList(name);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/getListToSelect")
    public ResultResponse<List<UiSelectOutVO>> getListToSelect(@RequestParam(value = "name",required = false) String name) throws Exception {
        List<UiSelectOutVO> list = Ui.getListToSelect(name);
        return ResultResponse.success(list);
    }

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() UiInputVO inputVO) throws Exception {
        String id= Ui.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/deleteById")
    public ResultResponse<Void> deleteById(@RequestParam(value = "id") String id) throws Exception {
        Ui.deleteById(id);
        return ResultResponse.success();
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
                           @RequestParam(value = "taskId") String taskId) throws Exception {
        String content= uiService.getContentByTaskId(taskId);
        ResponseUtil.writeString(response,content);
    }

    @GetMapping(value="/getContent")
    public void getContent(HttpServletResponse response,
                           @RequestParam(value = "id",required = false) String id,
                           @RequestParam(value = "taskId",required = false) String taskId
                           ) throws Exception {
        String content= uiService.getContent(id,taskId);
        ResponseUtil.writeString(response,content);
    }
}

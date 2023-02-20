package com.andy.zhflow.admin.processUi;

import com.andy.zhflow.processUi.ProcessUi;
import com.andy.zhflow.processUi.ProcessUiInputVO;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/admin/processUi")
public class ProcessUiController {

    @Autowired
    private ProcessUiService processUiService;

    @GetMapping(value="/getList")
    public ResultResponse<List<ProcessUi>> getList(@RequestParam(value = "name",required = false) String name) throws Exception {
        List<ProcessUi> list = ProcessUi.getList(name);
        return ResultResponse.success(list);
    }

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() ProcessUiInputVO inputVO) throws Exception {
        String id= ProcessUi.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getById")
    public ResultResponse<ProcessUi> getById(@RequestParam("id") String id) throws Exception {
        ProcessUi ui = ProcessUi.getById(id);
        return ResultResponse.success(ui);
    }
}

package com.andy.zhflow.admin.processUi;

import com.andy.zhflow.processUi.ProcessUi;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/admin/processUi")
public class ProcessUiController {

    @Autowired
    private ProcessUiService processUiService;

    @PostMapping(value="/save")
    public ResultResponse<String> save(@RequestBody() ProcessUiInputVO inputVO) throws Exception {
        String id= processUiService.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/del")
    public ResultResponse<String> del(@RequestParam("id") String id) throws Exception {
        ProcessUi.del(id);
        return ResultResponse.success();
    }

    @GetMapping(value="/getById")
    public ResultResponse<ProcessUi> getById(@RequestParam("id") String id) throws Exception {
        ProcessUi ui = ProcessUi.getById(id);
        return ResultResponse.success(ui);
    }

    @GetMapping(value="/getData")
    public ResultResponse<Map<String,Object>> getData(@RequestParam("id") String id) throws Exception {
        Map<String,Object> map=new HashMap<>();
        map.put("name","hello");
        return ResultResponse.success(map);
    }
}

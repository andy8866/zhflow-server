package com.andy.zhflow.admin.processUi;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

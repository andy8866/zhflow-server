package com.andy.zhflow.admin.index;

import com.andy.zhflow.base.response.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api")
public class IndexController {

    @GetMapping(value="/hello")
    public ResultResponse<String> hello() {
        return ResultResponse.success("hello");
    }
}

package com.andy.zhflow.admin.index;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/index")
public class IndexController {

    @GetMapping(value="/hello")
    public ResultResponse<String> hello() {
        return ResultResponse.success("hello");
    }

    @GetMapping(value="/validateToken")
    public ResultResponse<Void> validateToken() {
        return ResultResponse.success(null);
    }
}

package com.andy.zhflow.admin.third;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/third")
public class ThirdController {

    @Autowired
    private ThirdService thirdService;

    @GetMapping(value="/getAppToken")
    public ResultResponse<String> getAppToken() throws Exception {
        String token=thirdService.getAppToken();
        return ResultResponse.success(token);
    }
}

package com.andy.zhflow.admin.user;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.user.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/admin/user")
public class UserController {

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<User>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<User> appPage = User.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }
}

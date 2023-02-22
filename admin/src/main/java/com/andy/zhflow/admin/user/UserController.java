package com.andy.zhflow.admin.user;

import com.andy.zhflow.amis.AmisPage;
import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.user.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<User>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<User> appPage = User.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/getListNoPage")
    public ResultResponse<List<User>> getListNoPage(@RequestParam(value = "name",required = false) String name) {
        List<User> list = User.getListNoPage(name);

        return ResultResponse.success(list);
    }

    @GetMapping(value="/getListByIds")
    public ResultResponse<List<User>> getListByIds(@RequestParam(value = "ids") String ids) {
        List<User> list = User.getListByIds(ids);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/getCurrentUser")
    public ResultResponse<User> getCurrentUser() {
        return ResultResponse.success(userService.getCurrentUser());
    }

    @GetMapping(value="/switchCurrentUser")
    public ResultResponse<String> switchCurrentUser(@RequestParam(value = "id") String id) {
        String token=userService.switchCurrentUser(id);
        return ResultResponse.success(token);
    }
}

package com.scyingneng.zhflow.user;

import com.scyingneng.zhflow.amis.AmisPage;
import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.vo.SelectOutVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/getList")
    public ResultResponse<AmisPage<User>> getList(@RequestParam("page") Integer page, @RequestParam("perPage") Integer perPage) {
        IPage<User> appPage = User.selectPage(page, perPage);

        return ResultResponse.success(AmisPage.transitionPage(appPage));
    }

    @GetMapping(value="/addUser")
    public ResultResponse<Void> addUser(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {

        User.addUser(userName,password);

        return ResultResponse.success();
    }

    @GetMapping(value="/updatePassword")
    public ResultResponse<Void> updatePassword(@RequestParam("id") String id, @RequestParam("password") String password) throws Exception {

        User.updatePassword(id,password);

        return ResultResponse.success();
    }

    @GetMapping(value="/del")
    public ResultResponse<Void> del(@RequestParam("id") String id) throws Exception {

        User.del(id);

        return ResultResponse.success();
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

    @GetMapping(value="/getNameListByIds")
    public ResultResponse<String> getNameListByIds(@RequestParam(value = "ids") String ids) {
        String names = User.getNameListByIds(ids);
        return ResultResponse.success(names);
    }

    @GetMapping(value="/getCurrentUser")
    public ResultResponse<User> getCurrentUser() {
        return ResultResponse.success(userService.getCurrentUser());
    }

    @GetMapping(value="/switchUser")
    public ResultResponse<String> switchUser(@RequestParam(value = "id") String id) throws Exception {
        String token=userService.switchUser(id);
        return ResultResponse.success(token);
    }

    @GetMapping(value = "/getListToSelect")
    public ResultResponse<List<SelectOutVO>> getListToSelect(@RequestParam(value = "name", required = false) String name) {
        List<SelectOutVO> list = User.getListToSelect(name);
        return ResultResponse.success(list);
    }
}

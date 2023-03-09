package com.andy.zhflow.user;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/third/user")
public class UserApiController {

    @GetMapping(value = "/getListToSelect")
    public ResultResponse<List<UserSelectOutVO>> getListToSelect(@RequestParam(value = "name", required = false) String name) {
        List<UserSelectOutVO> list = User.getListToSelect(name);
        return ResultResponse.success(list);
    }

    /**
     * 获取上级用户
     * @return
     */
    @GetMapping(value = "/getSuperiorUserId")
    public ResultResponse<String> getSuperiorUserId(@RequestParam(value = "userId") String userId) {
        return ResultResponse.success("Qsw0NCe5n1RxA5Xh1c-td");
    }

    @GetMapping(value = "/getUserNameById")
    public ResultResponse<String> getUserNameById(@RequestParam(value = "userId") String userId) {
        return ResultResponse.success(User.getNameById(userId));
    }
}

package com.andy.zhflow.user;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/third/user")
public class UserApiController {

    @PostMapping(value = "/getListToSelect")
    public ResultResponse<List<UserSelectOutVO>> getListToSelect(@RequestBody Map<String,String> map) {
        String name=map.getOrDefault("name",null);
        List<UserSelectOutVO> list = User.getListToSelect(name);
        return ResultResponse.success(list);
    }

    /**
     * 获取上级用户
     * @return
     */
    @PostMapping(value = "/getSuperiorUserId")
    public ResultResponse<String> getSuperiorUserId(@RequestBody Map<String,String> map) {
        String userId=map.getOrDefault("userId",null);
        return ResultResponse.success("Qsw0NCe5n1RxA5Xh1c-td");
    }

    @PostMapping(value = "/getUserNameById")
    public ResultResponse<String> getUserNameById(@RequestBody Map<String,String> map) {
        String userId=map.getOrDefault("userId",null);
        return ResultResponse.success(User.getNameById(userId));
    }
}

package com.andy.zhflow.role;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/third/role")
public class RoleApiController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value="/getListToSelect")
    public ResultResponse<List<RoleSelectOutVO>> getListToSelect(@RequestParam(value = "name",required = false) String name) {
        List<RoleSelectOutVO> list = roleService.getListToSelect();
        return ResultResponse.success(list);
    }

}

package com.andy.zhflow.role;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value="/getList")
    public ResultResponse<List<RoleOutVO>> getList() {
        List<RoleOutVO> list = roleService.getList();

        return ResultResponse.success(list);
    }

    @GetMapping(value="/getListToSelect")
    public ResultResponse<List<RoleSelectOutVO>> getListToSelect() {
        List<RoleSelectOutVO> list = roleService.getListToSelect();
        return ResultResponse.success(list);
    }

}

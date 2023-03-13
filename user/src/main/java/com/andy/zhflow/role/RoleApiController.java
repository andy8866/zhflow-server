package com.andy.zhflow.role;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.vo.SelectOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/third/role")
public class RoleApiController {

    @Autowired
    private RoleService roleService;

    @PostMapping(value="/getListToSelect")
    public ResultResponse<List<SelectOutVO>> getListToSelect() {
        List<SelectOutVO> list = roleService.getListToSelect();
        return ResultResponse.success(list);
    }

}

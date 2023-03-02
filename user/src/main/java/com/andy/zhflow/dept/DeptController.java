package com.andy.zhflow.dept;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping(value="/getList")
    public ResultResponse<List<DeptOutVO>> getList() {
        List<DeptOutVO> list = deptService.getList();

        return ResultResponse.success(list);
    }

    @GetMapping(value="/getListToSelect")
    public ResultResponse<List<DeptSelectOutVO>> getListToSelect() {
        List<DeptSelectOutVO> list = deptService.getListToSelect();
        return ResultResponse.success(list);
    }

}

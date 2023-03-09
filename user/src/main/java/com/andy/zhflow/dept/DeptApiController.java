package com.andy.zhflow.dept;

import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/third/dept")
public class DeptApiController {

    @Autowired
    private DeptService deptService;

    @GetMapping(value="/getListToSelect")
    public ResultResponse<List<DeptSelectOutVO>> getListToSelect(@RequestParam(value = "name",required = false) String name) {
        List<DeptSelectOutVO> list = deptService.getListToSelect();
        return ResultResponse.success(list);
    }

}

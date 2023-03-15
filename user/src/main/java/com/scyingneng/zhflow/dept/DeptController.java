package com.scyingneng.zhflow.dept;

import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.vo.SelectOutVO;
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
    public ResultResponse<List<SelectOutVO>> getListToSelect() {
        List<SelectOutVO> list = deptService.getListToSelect();
        return ResultResponse.success(list);
    }
}

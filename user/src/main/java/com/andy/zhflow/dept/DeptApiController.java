package com.andy.zhflow.dept;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.vo.SelectOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/third/dept")
public class DeptApiController {

    @Autowired
    private DeptService deptService;

    @PostMapping(value="/getListToSelect")
    public ResultResponse<List<SelectOutVO>> getListToSelect() {
        List<SelectOutVO> list = deptService.getListToSelect();
        return ResultResponse.success(list);
    }

}

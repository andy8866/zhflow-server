package com.andy.zhflow.proc.copy;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.security.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/proc/copy")
public class CopyController {

    @Autowired
    private CopyService copyService;

    @GetMapping(value="/getList")
    public ResultResponse<List<Copy>> getList() {
        List<Copy> list = Copy.getList(UserUtil.getUserId());
        return ResultResponse.success(list);
    }

}

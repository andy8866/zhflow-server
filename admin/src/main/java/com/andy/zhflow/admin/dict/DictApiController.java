package com.andy.zhflow.admin.dict;

import com.andy.zhflow.dict.DictValue;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/third/dict")
public class DictApiController {

    @GetMapping(value="/getValueList")
    public ResultResponse<List<DictValue>> getValueList(@RequestParam("type") String type) {
        List<DictValue> list = DictValue.getListByType(type);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/getValueMap")
    public ResultResponse<Map<String,String>> getValueMap(@RequestParam("type") String type) {
        Map<String,String> map = DictValue.getValueMap(type);
        return ResultResponse.success(map);
    }
}

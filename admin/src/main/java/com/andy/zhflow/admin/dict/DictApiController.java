package com.andy.zhflow.admin.dict;

import com.andy.zhflow.dict.DictValue;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/third/dict")
public class DictApiController {

    @PostMapping(value="/getValueList")
    public ResultResponse<List<DictValue>> getValueList(@RequestBody Map<String,String> map) {
        String type=map.getOrDefault("type",null);
        List<DictValue> list = DictValue.getListByType(type);
        return ResultResponse.success(list);
    }

    @PostMapping(value="/getValueMap")
    public ResultResponse<Map<String,String>> getValueMap(@RequestBody Map<String,String> m) {
        String type=m.getOrDefault("type",null);
        Map<String,String> map = DictValue.getValueMap(type);
        return ResultResponse.success(map);
    }
}

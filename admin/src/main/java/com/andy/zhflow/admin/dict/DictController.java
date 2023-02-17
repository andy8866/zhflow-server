package com.andy.zhflow.admin.dict;

import com.andy.zhflow.dict.DictGroup;
import com.andy.zhflow.dict.DictGroupInputVO;
import com.andy.zhflow.dict.DictValue;
import com.andy.zhflow.dict.DictValueInputVO;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/admin/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @PostMapping(value="/saveGroup")
    public ResultResponse<String> saveGroup(@RequestBody() DictGroupInputVO inputVO) throws Exception {
        String id= DictGroup.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getGroupList")
    public ResultResponse<List<DictGroup>> getGroupList() {
        List<DictGroup> list = DictGroup.getList();
        return ResultResponse.success(list);
    }

    @GetMapping(value="/delGroup")
    public ResultResponse<Void> delGroup(@RequestParam("id") String id) {
        dictService.delGroupById(id);
        return ResultResponse.success();
    }

    @PostMapping(value="/saveValue")
    public ResultResponse<String> saveValue(@RequestBody() DictValueInputVO inputVO) throws Exception {
        String id= DictValue.save(inputVO);
        return ResultResponse.success(id);
    }

    @GetMapping(value="/getValueList")
    public ResultResponse<List<DictValue>> getValueList(@RequestParam("groupType") String groupType) {
        List<DictValue> list = DictValue.getListByGroupType(groupType);
        return ResultResponse.success(list);
    }

    @GetMapping(value="/delValue")
    public ResultResponse<Void> delValue(@RequestParam("id") String id) {
        DictValue.del(id);
        return ResultResponse.success();
    }
}

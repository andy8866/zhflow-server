package com.andy.zhflow.admin.dict;

import com.andy.zhflow.dict.DictGroup;
import com.andy.zhflow.dict.DictValue;
import com.andy.zhflow.response.ResultResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
public class DictService {

    public void delGroupById(String id) {
        DictGroup group = DictGroup.getById(id);
        if(group==null) return ;

        DictGroup.del(id);
        DictValue.delByType(group.getType());
    }
}

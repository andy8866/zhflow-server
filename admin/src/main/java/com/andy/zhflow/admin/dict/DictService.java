package com.andy.zhflow.admin.dict;

import com.andy.zhflow.dict.DictGroup;
import com.andy.zhflow.dict.DictValue;
import org.springframework.stereotype.Component;

@Component
public class DictService {

    public void delGroupById(String id) {
        DictGroup group = DictGroup.getById(id);
        if(group==null) return ;

        DictGroup.del(id);
        DictValue.delByType(group.getType());
    }

}

package com.andy.zhflow.dict;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@TableName("dict_value")
public class DictValue extends BaseEntity {
    private static DictValueMapper dictValueMapper;

    @Autowired
    public void setDictGroupMapper(DictValueMapper mapper){
        dictValueMapper =mapper;
    }

    private String type;

    private String name;

    private String value;


    public static String save(DictValueInputVO inputVO) throws Exception {

        DictValue item =new DictValue();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = dictValueMapper.selectById(inputVO.getId());

        item.setBase(true);

        item.setType(inputVO.getType());
        item.setName(inputVO.getName());
        item.setValue(inputVO.getValue());

        if(item.getIsNew()){
            dictValueMapper.insert(item);
        }
        else{
            dictValueMapper.updateById(item);
        }

        return item.getId();
    }

    public static void del(String id){
        dictValueMapper.deleteById(id);
    }
    public static void delByType(String type) {
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<DictValue>().eq(DictValue::getType,type);
        dictValueMapper.delete(wrapper);
    }


    public static List<DictValue> getListByType(String type) {
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<DictValue>().orderByDesc(DictValue::getCreateTime)
                .eq(DictValue::getType,type);

        return dictValueMapper.selectList(wrapper);
    }

    public static Map<String, String> getValueMap(String type) {
        List<DictValue> list=getListByType(type);
        Map<String,String> map=new HashMap<>();
        for (DictValue dictValue:list){
            map.put(dictValue.getValue(),dictValue.getName());
        }

        return map;
    }
}

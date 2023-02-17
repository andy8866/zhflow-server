package com.andy.zhflow.dict;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@TableName("dict_value")
public class DictValue extends BaseEntity {
    private static DictValueMapper dictValueMapper;

    @Autowired
    public void setDictGroupMapper(DictValueMapper mapper){
        dictValueMapper =mapper;
    }

    private String groupType;

    private String name;

    private String value;

    public static String save(DictValueInputVO inputVO) throws Exception {

        DictValue item =new DictValue();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = dictValueMapper.selectById(inputVO.getId());

        item.setGroupType(inputVO.getGroupType());
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
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<DictValue>().eq(DictValue::getGroupType,type);
        dictValueMapper.delete(wrapper);
    }


    public static List<DictValue> getListByGroupType( String groupType) {
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<DictValue>().orderByDesc(DictValue::getCreateTime)
                .eq(DictValue::getGroupType,groupType);

        return dictValueMapper.selectList(wrapper);
    }
}

package com.andy.zhflow.dict;

import com.andy.zhflow.entity.BaseAppEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@TableName("dict_value")
public class DictValue extends BaseAppEntity {
    private static DictValueMapper dictValueMapper;

    @Autowired
    public void setDictGroupMapper(DictValueMapper mapper){
        dictValueMapper =mapper;
    }

    private String groupType;

    private String name;

    private String value;

    public static String save(DictValueInputVO inputVO) throws Exception {

        if(StringUtils.isEmpty(inputVO.getName())){
            throw new Exception("缺少名称");
        }

        if(StringUtils.isEmpty(inputVO.getValue())){
            throw new Exception("缺少值");
        }

        DictValue item =new DictValue();
        if(StringUtils.isNoneEmpty(inputVO.getId())){
            item = dictValueMapper.selectById(inputVO.getId());
            if(item ==null){
                throw new Exception("未查到id数据");
            }
        }

        item.setBase(true,item.getAppId());

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
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DictValue::getGroupType,type);
        dictValueMapper.delete(wrapper);
    }


    public static List<DictValue> getListByGroupType(String appId, String groupType) {
        LambdaQueryWrapper<DictValue> wrapper=new LambdaQueryWrapper<DictValue>().orderByDesc(DictValue::getCreateTime)
                .eq(DictValue::getGroupType,groupType);

        if(StringUtils.isNotEmpty(appId)) wrapper.eq(DictValue::getAppId,appId);
        return dictValueMapper.selectList(wrapper);
    }
}

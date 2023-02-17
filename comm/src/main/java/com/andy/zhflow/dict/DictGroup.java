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
@TableName("dict_group")
public class DictGroup extends BaseEntity {
    private static DictGroupMapper dictGroupMapper;

    @Autowired
    public void setDictGroupMapper(DictGroupMapper mapper){
        dictGroupMapper =mapper;
    }

    private String type;

    private String name;

    public static String save(DictGroupInputVO inputVO) throws Exception {

        DictGroup item =new DictGroup();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = dictGroupMapper.selectById(inputVO.getId());

        item.setBase(true);

        item.setType(inputVO.getType());
        item.setName(inputVO.getName());

        if(item.getIsNew()){
            dictGroupMapper.insert(item);
        }
        else{
            dictGroupMapper.updateById(item);
        }

        return item.getId();
    }

    public static void del(String id){
        dictGroupMapper.deleteById(id);
    }
    public static DictGroup getById(String id) {
       return dictGroupMapper.selectById(id);
    }

    public static List<DictGroup> getList() {
        LambdaQueryWrapper<DictGroup> wrapper=new LambdaQueryWrapper<DictGroup>().orderByDesc(DictGroup::getCreateTime);
        return dictGroupMapper.selectList(wrapper);
    }
}

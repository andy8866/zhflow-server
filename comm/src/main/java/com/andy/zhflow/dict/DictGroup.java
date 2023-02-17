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
@TableName("dict_group")
public class DictGroup extends BaseAppEntity {
    private static DictGroupMapper dictGroupMapper;

    @Autowired
    public void setDictGroupMapper(DictGroupMapper mapper){
        dictGroupMapper =mapper;
    }

    private String type;

    private String name;

    public static String save(DictGroupInputVO inputVO) throws Exception {

        if(StringUtils.isEmpty(inputVO.getType())){
            throw new Exception("缺少类型");
        }

        if(StringUtils.isEmpty(inputVO.getName())){
            throw new Exception("缺少名称");
        }

        DictGroup item =new DictGroup();
        if(StringUtils.isNotEmpty(inputVO.getId())){
            item = dictGroupMapper.selectById(inputVO.getId());
            if(item ==null){
                throw new Exception("未查到id数据");
            }
        }

        item.setBase(true, inputVO.getAppId());

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

    public static List<DictGroup> getList(String appId) {
        LambdaQueryWrapper<DictGroup> wrapper=new LambdaQueryWrapper<DictGroup>().orderByDesc(DictGroup::getCreateTime);
        if(StringUtils.isNotEmpty(appId)) wrapper.eq(DictGroup::getAppId,appId);
        return dictGroupMapper.selectList(wrapper);
    }
}

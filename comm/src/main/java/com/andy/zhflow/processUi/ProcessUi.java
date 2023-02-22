package com.andy.zhflow.processUi;

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
@TableName("process_ui")
public class ProcessUi extends BaseEntity {
    private static ProcessUiMapper processUiMapper;


    @Autowired
    public void setProcessUiMapper(ProcessUiMapper mapper){
        processUiMapper =mapper;
    }

    private String name;
    private String type;
    private String code;
    private String content;

    public static String save(ProcessUiInputVO inputVO) throws Exception {
        ProcessUi processUi =new ProcessUi();
        if(StringUtils.isNotEmpty(inputVO.getId())) processUi = processUiMapper.selectById(inputVO.getId());

        processUi.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) processUi.setName(inputVO.getName());
        if(StringUtils.isNotEmpty(inputVO.getType())) processUi.setType(inputVO.getType());
        if(StringUtils.isNotEmpty(inputVO.getContent())) processUi.setContent(inputVO.getContent());
        if(StringUtils.isNotEmpty(inputVO.getCode())) processUi.setCode(inputVO.getCode());

        if(processUi.getIsNew()){
            processUiMapper.insert(processUi);
        }
        else{
            processUiMapper.updateById(processUi);
        }

        return processUi.getId();
    }

    public static ProcessUi getById(String id) {
        return processUiMapper.selectById(id);
    }

    public static List<ProcessUi> getList(String name,String type,String noType) {
        LambdaQueryWrapper<ProcessUi> wrapper=new LambdaQueryWrapper<ProcessUi>().orderByDesc(ProcessUi::getCreateTime);
        if(StringUtils.isNotEmpty(name)) wrapper.like(ProcessUi::getName,name);
        if(StringUtils.isNotEmpty(type)) wrapper.eq(ProcessUi::getType,type);
        if(StringUtils.isNotEmpty(noType)) wrapper.notIn(ProcessUi::getType,noType.split(","));

        return processUiMapper.selectList(wrapper);
    }


    public static ProcessUi getByCode(String code) {
        LambdaQueryWrapper<ProcessUi> wrapper=new LambdaQueryWrapper<ProcessUi>().eq(ProcessUi::getCode,code);
        return processUiMapper.selectOne(wrapper);
    }


}

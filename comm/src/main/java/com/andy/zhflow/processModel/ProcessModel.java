package com.andy.zhflow.processModel;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@TableName("process_model")
public class ProcessModel extends BaseEntity {
    private static ProcessModelMapper processModelMapper;


    @Autowired
    public void setProcessModelMapper(ProcessModelMapper mapper){
        processModelMapper =mapper;
    }

    private String name;
    private String content;

    private String processKey;

    private Date deploymentTime;

    private Boolean asTemplate=null;

    public static String save(ProcessModelInputVO inputVO) throws Exception {
        ProcessModel processModel =new ProcessModel();
        if(StringUtils.isNoneEmpty(inputVO.getId())) processModel = processModelMapper.selectById(inputVO.getId());

        processModel.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) processModel.setName(inputVO.getName());

        if(StringUtils.isNotEmpty(inputVO.getContent())) processModel.setContent(inputVO.getContent());

        if(StringUtils.isNotEmpty(inputVO.getProcessKey())) processModel.setProcessKey(inputVO.getProcessKey());

        if(inputVO.getAsTemplate()!=null) processModel.setAsTemplate(inputVO.getAsTemplate());

        if(processModel.getIsNew()){
            processModelMapper.insert(processModel);
        }
        else{
            processModelMapper.updateById(processModel);
        }

        return processModel.getId();
    }

    public String save(){
        setBase(true);

        if(getIsNew()){
            processModelMapper.insert(this);
        }
        else{
            processModelMapper.updateById(this);
        }

        return getId();
    }

    public static IPage<ProcessModel> selectPage(Integer page, Integer perPage,Boolean asTemplate){
        LambdaQueryWrapper<ProcessModel> wrapper=new LambdaQueryWrapper<ProcessModel>().orderByDesc(ProcessModel::getCreateTime);
        if(asTemplate!=null) wrapper.eq(ProcessModel::getAsTemplate,asTemplate);

        Page<ProcessModel> item = processModelMapper.selectPage(new Page<>(page, perPage), wrapper);
        return item;
    }

    public static void del(String id){
        processModelMapper.deleteById(id);
    }

    public static ProcessModel getById(String id) {
        return processModelMapper.selectById(id);
    }

    public static ProcessModel getByKey(String key) {
        LambdaQueryWrapper<ProcessModel> lambdaQueryWrapper=new LambdaQueryWrapper<ProcessModel>().eq(ProcessModel::getProcessKey,key);
        return processModelMapper.selectOne(lambdaQueryWrapper);
    }

}

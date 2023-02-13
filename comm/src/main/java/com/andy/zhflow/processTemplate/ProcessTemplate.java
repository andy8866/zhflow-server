package com.andy.zhflow.processTemplate;

import com.andy.zhflow.entity.BaseAppEntity;
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
@TableName("process_template")
public class ProcessTemplate extends BaseEntity {
    private static ProcessTemplateMapper processTemplateMapper;


    @Autowired
    public void setProcessTemplateMapper(ProcessTemplateMapper mapper){
        processTemplateMapper =mapper;
    }

    private String name;
    private String content;

    private String processKey;

    private String catalog;

    public static String save(String id,String name,String content,String key,String catalog) throws Exception {
        ProcessTemplate processTemplate =new ProcessTemplate();
        if(StringUtils.isNoneEmpty(id)){
            processTemplate = processTemplateMapper.selectById(id);
            if(processTemplate ==null){
                throw new Exception("未查到id数据");
            }
        }

        processTemplate.setBase(true);

        if(StringUtils.isNoneEmpty(name)){
            processTemplate.setName(name);
        }

        if(StringUtils.isNoneEmpty(content)){
            processTemplate.setContent(content);
        }

        if(StringUtils.isNoneEmpty(key)){
            processTemplate.setProcessKey(key);
        }

        if(StringUtils.isNoneEmpty(catalog)){
            processTemplate.setCatalog(catalog);
        }

        if(processTemplate.getIsNew()){
            processTemplateMapper.insert(processTemplate);
        }
        else{
            processTemplateMapper.updateById(processTemplate);
        }

        return processTemplate.getId();
    }

    public String save(){
        setBase(true);

        if(getIsNew()){
            processTemplateMapper.insert(this);
        }
        else{
            processTemplateMapper.updateById(this);
        }

        return getId();
    }

    public static IPage<ProcessTemplate> selectPage(Integer page, Integer perPage){
        LambdaQueryWrapper<ProcessTemplate> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(ProcessTemplate::getCreateTime);

        Page<ProcessTemplate> appPage = processTemplateMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return appPage;
    }

    public static void del(String id){
        processTemplateMapper.deleteById(id);
    }

    public static ProcessTemplate getById(String id) {
        return processTemplateMapper.selectById(id);
    }

    public static ProcessTemplate getByKey(String key) {
        LambdaQueryWrapper<ProcessTemplate> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProcessTemplate::getProcessKey,key);
        return processTemplateMapper.selectOne(lambdaQueryWrapper);
    }

}

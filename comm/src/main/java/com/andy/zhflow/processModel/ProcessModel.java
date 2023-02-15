package com.andy.zhflow.processModel;

import com.andy.zhflow.entity.BaseAppEntity;
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
public class ProcessModel extends BaseAppEntity {
    private static ProcessModelMapper processModelMapper;


    @Autowired
    public void setProcessModelMapper(ProcessModelMapper mapper){
        processModelMapper =mapper;
    }

    private String name;
    private String content;

    private String processKey;

    private Date deploymentTime;

    public boolean getAsTemplate() {
        return asTemplate;
    }

    public void setAsTemplate(boolean asTemplate) {
        this.asTemplate = asTemplate;
    }

    private boolean asTemplate;

    public static String save(ProcessModelInputVO inputVO) throws Exception {
        ProcessModel processModel =new ProcessModel();
        if(StringUtils.isNoneEmpty(inputVO.getId())){
            processModel = processModelMapper.selectById(inputVO.getId());
            if(processModel ==null){
                throw new Exception("未查到id数据");
            }
        }

        processModel.setBase(true);

        if(StringUtils.isNoneEmpty(inputVO.getName())){
            processModel.setName(inputVO.getName());
        }

        if(StringUtils.isNoneEmpty(inputVO.getContent())){
            processModel.setContent(inputVO.getContent());
        }

        if(StringUtils.isNoneEmpty(inputVO.getProcessKey())){
            processModel.setProcessKey(inputVO.getProcessKey());
        }

        if(inputVO.getAsTemplate()){
            processModel.setAsTemplate(true);
        }

        LambdaQueryWrapper<ProcessModel> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProcessModel::getName,inputVO.getName())
                .ne(ProcessModel::getId,processModel.getId());

        if(inputVO.getAsTemplate()){
            lambdaQueryWrapper.eq(ProcessModel::getAppId,"0");
        }else {
            lambdaQueryWrapper.eq(ProcessModel::getAppId, inputVO.getAppId());
        }

        if(processModelMapper.exists(lambdaQueryWrapper)){
            throw new Exception("名称已存在");
        }

        if(processModel.getIsNew()){
            if(!processModel.getAsTemplate()){
                processModel.setAppId(inputVO.getAppId());
            }
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

    public static IPage<ProcessModel> selectPage(Integer page, Integer perPage,
                                                 String appId,Boolean asTemplate){
        LambdaQueryWrapper<ProcessModel> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(ProcessModel::getCreateTime);

        if(asTemplate!=null){
            lambdaQueryWrapper.eq(ProcessModel::getAsTemplate,asTemplate);
            appId=null;
        }

        if(StringUtils.isNoneEmpty(appId)){
            lambdaQueryWrapper.eq(ProcessModel::getAppId,appId);
        }

        Page<ProcessModel> appPage = processModelMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return appPage;
    }

    public static void del(String id){
        processModelMapper.deleteById(id);
    }

    public static ProcessModel getById(String id) {
        return processModelMapper.selectById(id);
    }

    public static ProcessModel getByKey(String key) {
        LambdaQueryWrapper<ProcessModel> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProcessModel::getProcessKey,key);
        return processModelMapper.selectOne(lambdaQueryWrapper);
    }

}

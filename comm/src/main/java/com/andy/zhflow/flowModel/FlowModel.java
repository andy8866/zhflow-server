package com.andy.zhflow.flowModel;

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
@TableName("flow_model")
public class FlowModel extends BaseAppEntity {
    private static FlowModelMapper flowModelMapper;


    @Autowired
    public void setFlowModelMapper(FlowModelMapper mapper){
        flowModelMapper =mapper;
    }

    private String name;
    private String content;

    private String processKey;

    private Date deploymentTime;

    public static String save(String id,String appId,String name,String content,String key) throws Exception {
        FlowModel flowModel =new FlowModel();
        if(StringUtils.isNoneEmpty(id)){
            flowModel = flowModelMapper.selectById(id);
            if(flowModel ==null){
                throw new Exception("未查到id数据");
            }
        }

        flowModel.setBase(true);

        if(StringUtils.isNoneEmpty(name)){
            flowModel.setName(name);
        }

        if(StringUtils.isNoneEmpty(content)){
            flowModel.setContent(content);
        }

        if(StringUtils.isNoneEmpty(key)){
            flowModel.setProcessKey(key);
        }

        if(flowModel.getIsNew()){
            flowModel.setAppId(appId);
            flowModelMapper.insert(flowModel);
        }
        else{
            flowModelMapper.updateById(flowModel);
        }

        return flowModel.getId();
    }

    public String save(){
        setBase(true);

        if(getIsNew()){
            flowModelMapper.insert(this);
        }
        else{
            flowModelMapper.updateById(this);
        }

        return getId();
    }

    public static IPage<FlowModel> selectPage(Integer page, Integer perPage,
                                              String appId){
        LambdaQueryWrapper<FlowModel> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(FlowModel::getCreateTime);

        if(StringUtils.isNoneEmpty(appId)){
            lambdaQueryWrapper.eq(FlowModel::getAppId,appId);
        }
        Page<FlowModel> appPage = flowModelMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return appPage;
    }

    public static void del(String id){
        flowModelMapper.deleteById(id);
    }

    public static FlowModel getById(String id) {
        return flowModelMapper.selectById(id);
    }

}

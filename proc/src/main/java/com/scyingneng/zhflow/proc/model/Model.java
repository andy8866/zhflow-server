package com.scyingneng.zhflow.proc.model;

import com.scyingneng.zhflow.entity.AppEntity;
import com.scyingneng.zhflow.service.security.IAuthService;
import com.scyingneng.zhflow.third.app.App;
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
@TableName("proc_model")
public class Model extends AppEntity {
    private static ModelMapper modelMapper;
    @Autowired
    public void setModelMapper(ModelMapper mapper){
        modelMapper =mapper;
    }

    private static IAuthService authService;
    @Autowired
    public void setIAuthService(IAuthService authService){
        Model.authService =authService;
    }

    private String name;
    private String content;

    private String procKey;

    private Date deploymentTime;

    public static String save(ModelInputVO inputVO) throws Exception {
        Model model =new Model();
        if(StringUtils.isNoneEmpty(inputVO.getId())) model = modelMapper.selectById(inputVO.getId());

        model.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) model.setName(inputVO.getName());

        if(StringUtils.isNotEmpty(inputVO.getContent())) model.setContent(inputVO.getContent());

        if(StringUtils.isNotEmpty(inputVO.getProcKey())) model.setProcKey(inputVO.getProcKey());

        if(StringUtils.isNotEmpty(authService.getAppId())) {
            model.setAppId(authService.getAppId());
            model.setAppName(App.getName(authService.getAppId()));
        }
        if(StringUtils.isNotEmpty(authService.getUserId())) model.setCreateUserId(authService.getUserId());

        if(model.getIsNew()){
            modelMapper.insert(model);
        }
        else{
            modelMapper.updateById(model);
        }

        return model.getId();
    }

    public String save(){
        setBase(true);

        if(getIsNew()){
            modelMapper.insert(this);
        }
        else{
            modelMapper.updateById(this);
        }

        return getId();
    }

    public static IPage<Model> selectPage(Integer page, Integer perPage,String appId){
        LambdaQueryWrapper<Model> wrapper=new LambdaQueryWrapper<Model>().orderByDesc(Model::getCreateTime);
        if(StringUtils.isNotEmpty(appId)) wrapper.eq(Model::getAppId,appId);

        Page<Model> item = modelMapper.selectPage(new Page<>(page, perPage), wrapper);
        return item;
    }

    public static void del(String id){
        modelMapper.deleteById(id);
    }

    public static Model getById(String id) {
        return modelMapper.selectById(id);
    }

    public static Model getByKey(String key) {
        LambdaQueryWrapper<Model> lambdaQueryWrapper=new LambdaQueryWrapper<Model>().eq(Model::getProcKey,key);
        return modelMapper.selectOne(lambdaQueryWrapper);
    }

}

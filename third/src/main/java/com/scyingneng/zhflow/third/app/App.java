package com.scyingneng.zhflow.third.app;

import com.scyingneng.zhflow.entity.BaseEntity;
import com.scyingneng.zhflow.third.appConfig.AppConfig;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@TableName("app")
public class App extends BaseEntity {

    private static AppMapper appMapper;

    @Autowired
    public void setAppMapper(AppMapper mapper){
        appMapper =mapper;
    }

    private String name;
    private String secretKey;
    private String rootUrl;
    private Boolean canDel;

    public static App getApp(String appId){
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<App>()
                .eq(App::getId,appId);

        return appMapper.selectOne(wrapper);
    }

    public static String getAppKey(String appId){
        return getApp(appId).getSecretKey();
    }

    public static String getName(String appId) {
        return getApp(appId).getName();
    }

    public static List<App> getList() {
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<App>().orderByDesc(App::getCreateTime);
        return appMapper.selectList(wrapper);
    }

    public static String save(AppInputVO inputVO) {
        App item =new App();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = appMapper.selectById(inputVO.getId());

        item.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) item.setName(inputVO.getName());
        if(StringUtils.isNotEmpty(inputVO.getSecretKey())) item.setSecretKey(inputVO.getSecretKey());
        if(StringUtils.isNotEmpty(inputVO.getRootUrl())) item.setRootUrl(inputVO.getRootUrl());

        if(item.getIsNew()){
            appMapper.insert(item);
        }
        else{
            appMapper.updateById(item);
        }

        return item.getId();
    }

    public static void del(String id) {
        App app = appMapper.selectById(id);
        if(!app.canDel) return ;

        appMapper.deleteById(id);

        AppConfig.delByAppId(id);
    }
}

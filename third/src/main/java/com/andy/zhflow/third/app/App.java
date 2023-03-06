package com.andy.zhflow.third.app;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}

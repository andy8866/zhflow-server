package com.andy.zhflow.third.appConfig;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("app_config")
public class AppConfig extends BaseEntity {

    private static AppConfigMapper appConfigMapper;

    @Autowired
    public void setAppConfigMapper(AppConfigMapper mapper){
        appConfigMapper =mapper;
    }

    private String appId;
    private String type;
    private String code;

    private String value;

    public static AppConfig getConfig(String appId, String code){
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId)
                .eq(AppConfig::getCode,code);

        return appConfigMapper.selectOne(wrapper);
    }

    public static String getValue(String appId,String code){
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId)
                .eq(AppConfig::getCode,code);

        AppConfig appConfig = getConfig(appId,code);
        if(appConfig==null) return null;

        return appConfig.getValue();
    }

    public static Object getVO(String appId,String code){
        AppConfig appConfig = getConfig(appId,code);
        if(appConfig==null) return null;

        switch (appConfig.getType()){
            case AppConfigConstant.TYPE_HTTP_REQUEST :{
                return JSON.parseObject(appConfig.getValue(), AppConfigHttpRequestVO.class);
            }
        }

        return null;
    }
}

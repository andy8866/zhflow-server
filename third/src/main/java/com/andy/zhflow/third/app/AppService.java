package com.andy.zhflow.third.app;

import com.andy.zhflow.third.appConfig.AppConfig;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppService {

    public void copyConfig(AppConfigCopyInputVO inputVO) {
        List<AppConfig> list=AppConfig.getList(inputVO.getId(),null);
        for (AppConfig appConfig:list){
            appConfig.setBase(true);
            appConfig.setId(NanoIdUtils.randomNanoId());
            appConfig.setAppId(inputVO.getTargetId());
            appConfig.insert();
        }
    }
}

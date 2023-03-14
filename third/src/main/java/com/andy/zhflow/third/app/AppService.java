package com.andy.zhflow.third.app;

import com.andy.zhflow.service.app.IAppService;
import com.andy.zhflow.service.thirdApp.IAppTokenService;
import com.andy.zhflow.service.thirdApp.IThirdAppService;
import com.andy.zhflow.third.appConfig.AppConfig;
import com.andy.zhflow.vo.SelectOutVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppService implements IAppService {

    @Autowired
    private IAppTokenService appTokenService;

    @Autowired
    private IThirdAppService thirdAppService;

    public void copyConfig(AppConfigCopyInputVO inputVO) {
        List<AppConfig> list=AppConfig.getList(inputVO.getId(),null);
        for (AppConfig appConfig:list){
            appConfig.setBase(true);
            appConfig.setId(NanoIdUtils.randomNanoId());
            appConfig.setAppId(inputVO.getTargetId());
            appConfig.insert();
        }
    }

    public String getFirstAppId() {
        List<App> list = App.getList();
        if(list.size()>0) return list.get(0).getId();
        return null;
    }

    public String switchApp(String appId) throws Exception {
        List<SelectOutVO> userListToSelect = thirdAppService.getUserListToSelect(appId);
        String userId=null;
        if(userListToSelect.size()>0) userId=userListToSelect.get(0).getValue();
        return appTokenService.getAppToken(appId,userId);
    }
}

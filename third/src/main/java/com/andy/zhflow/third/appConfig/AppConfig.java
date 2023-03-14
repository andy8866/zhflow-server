package com.andy.zhflow.third.appConfig;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private String name;

    private String httpUrlPath;
    private Boolean canDel;

    public static List<AppConfig> getList(String appId,String type) {
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId).orderByDesc(AppConfig::getCreateTime);

        if(StringUtils.isNotEmpty(type)) wrapper.eq(AppConfig::getType,type);

        return appConfigMapper.selectList(wrapper);
    }

    public static String save(AppConfigInputVO inputVO) {
        AppConfig item =new AppConfig();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = appConfigMapper.selectById(inputVO.getId());

        item.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getAppId())) item.setAppId(inputVO.getAppId());
        if(StringUtils.isNotEmpty(inputVO.getType())) item.setType(inputVO.getType());
        if(StringUtils.isNotEmpty(inputVO.getCode())) item.setCode(inputVO.getCode());
        if(StringUtils.isNotEmpty(inputVO.getName())) item.setName(inputVO.getName());

        if(StringUtils.isNotEmpty(inputVO.getHttpUrlPath())) item.setHttpUrlPath(inputVO.getHttpUrlPath());

        if(item.getIsNew()){
            appConfigMapper.insert(item);
        }
        else{
            appConfigMapper.updateById(item);
        }

        return item.getId();
    }

    public void insert(){
        appConfigMapper.insert(this);
    }


    public static AppConfig getConfig(String appId, String code){
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId)
                .eq(AppConfig::getCode,code);

        return appConfigMapper.selectOne(wrapper);
    }

    public static void delById(String id){

        AppConfig appConfig = appConfigMapper.selectById(id);
        if(!appConfig.getCanDel()) return ;

        appConfigMapper.deleteById(id);
    }

    public static void delByAppId(String appId){
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId);

        appConfigMapper.delete(wrapper);
    }

    public static void delByAppIdAndCode(String appId, String code) {
        LambdaQueryWrapper<AppConfig> wrapper = new LambdaQueryWrapper<AppConfig>()
                .eq(AppConfig::getAppId,appId).eq(AppConfig::getCode,code);

        appConfigMapper.delete(wrapper);
    }
}

package com.andy.zhflow.appuser;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("app_user")
public class AppUser extends BaseEntity {
    private static AppUserMapper appUserMapper;

    @Autowired
    public void setAppUserMapper(AppUserMapper mapper){
        appUserMapper =mapper;
    }

    private String userId;
    private String userName;
    private String appId;
    private String appName;


    public static AppUser getById(String id){
        return appUserMapper.selectById(id);
    }

    public static String save(AppUser appUser) throws Exception {
        if(StringUtils.isEmpty(appUser.getUserId())){
            throw new Exception("缺少用户ID");
        }
        if(StringUtils.isEmpty(appUser.getAppId())){
            throw new Exception("缺少AppID");
        }

        appUser.setBase(true);
        if(appUser.getIsNew()){
            appUserMapper.insert(appUser);
        }
        else{
            appUserMapper.updateById(appUser);
        }

        return appUser.getId();
    }

    public static IPage<AppUser> selectPage(Integer page, Integer perPage){
        LambdaQueryWrapper<AppUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(AppUser::getCreateTime);
        Page<AppUser> appPage = appUserMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return appPage;
    }
}

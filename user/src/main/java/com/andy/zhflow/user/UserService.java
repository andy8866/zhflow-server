package com.andy.zhflow.user;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.config.BaseConfig;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.SecurityUser;
import com.andy.zhflow.service.app.IAppService;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.service.user.UserOutVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class UserService implements IUserService {

    @Resource
    private IAppService appService;

    @Autowired
    private IAuthService authService;

    public User getCurrentUser() {
        String id= authService.getUserId();
        return User.getById(id);
    }

    public String switchUser(String id) throws Exception {
        return appService.switchApp(authService.getAppId(),id);
    }

    @Override
    public UserOutVO getByUserName(String userName) {
        User user = User.getByUserName(userName);
        UserOutVO outVO=new UserOutVO();
        BeanUtils.copyProperties(user,outVO);
        return outVO;
    }
}

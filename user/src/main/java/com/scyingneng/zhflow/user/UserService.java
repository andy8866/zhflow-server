package com.scyingneng.zhflow.user;

import com.scyingneng.zhflow.service.app.IAppService;
import com.scyingneng.zhflow.service.security.IAuthService;
import com.scyingneng.zhflow.service.user.IUserService;
import com.scyingneng.zhflow.service.user.UserOutVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

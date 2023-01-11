package com.andy.zhflow.admin.login;

import com.andy.zhflow.auth.AuthService;
import com.andy.zhflow.user.User;
import com.andy.zhflow.exception.ErrMsgException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    @Autowired
    private AuthService authService;

    public String login(String userName, String password) throws ErrMsgException {
        User user=User.getByUserName(userName);
        if(ObjectUtils.isEmpty(user)){
            throw new ErrMsgException("账号不存在");
        }

        if(!user. matchingPassword(password)){
            throw new ErrMsgException("密码错误");
        }

        String token=authService.createToken(user);
        return token;
    }

}

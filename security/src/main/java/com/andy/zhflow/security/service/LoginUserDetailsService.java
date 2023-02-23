package com.andy.zhflow.security.service;

import com.andy.zhflow.security.SecurityUser;
import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.service.user.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserOutVO user = userService.getByUserName(userName);
        if(user==null){
            throw new UsernameNotFoundException("");
        }

        return new SecurityUser(user.getId(),user.getUserName(),user.getPassword(),"ROLE_admin");
    }
}

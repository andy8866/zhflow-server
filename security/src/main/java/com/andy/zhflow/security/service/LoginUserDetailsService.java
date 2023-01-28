package com.andy.zhflow.security.service;

import com.andy.zhflow.security.jwt.JwtUser;
import com.andy.zhflow.user.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=User.getByUserName(userName);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("账号不存在");
        }

        return new JwtUser(user.getId(),user.getUserName(),user.getPassword(),"ROLE_admin");
    }
}

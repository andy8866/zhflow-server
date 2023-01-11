package com.andy.zhflow.user;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("user")
public class User extends BaseEntity {

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper mapper){
        userMapper=mapper;
    }

    private String userName;
    private String password;

    public static User getByUserName(String userName){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUserName,userName);
        return userMapper.selectOne(queryWrapper);
    }

    public boolean matchingPassword(String password){
        if(StringUtils.isEmpty(password)){
            return false;
        }

        if(password.equals(this.password)){
            return true;
        }

        return false;
    }
}

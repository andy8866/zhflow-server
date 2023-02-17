package com.andy.zhflow.user;

import com.alibaba.fastjson2.annotation.JSONField;
import com.andy.zhflow.entity.BaseEntity;
import com.andy.zhflow.suggest.Suggest;
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
@TableName("user")
public class User extends BaseEntity {

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper mapper){
        userMapper=mapper;
    }

    private String userName;

    @JSONField(serialize = false)
    private String password;

    public static User getByUserName(String userName){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().eq(User::getUserName,userName);
        return userMapper.selectOne(queryWrapper);
    }

    public boolean matchingPassword(String password){
        if(StringUtils.isEmpty(password)) return false;

        if(password.equals(this.password)) return true;

        return false;
    }

    public static IPage<User> selectPage(Integer page, Integer perPage){
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime);
        Page<User> suggestPage = userMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return suggestPage;
    }

    public static String getNameById(String id){
        User user = userMapper.selectById(id);
        if(user!=null) return user.getUserName();

        return null;
    }
}

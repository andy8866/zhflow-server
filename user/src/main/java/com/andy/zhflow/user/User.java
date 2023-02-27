package com.andy.zhflow.user;

import com.alibaba.fastjson2.annotation.JSONField;
import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime);
        Page<User> suggestPage = userMapper.selectPage(new Page<>(page, perPage), wrapper);
        return suggestPage;
    }

    public static List<User> getListNoPage(String name) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime);
        if(StringUtils.isNotEmpty(name)) wrapper.like(User::getUserName,name);
        return userMapper.selectList(wrapper);
    }

    public static String getNameById(String id){
        User user = getById(id);
        if(user!=null) return user.getUserName();

        return null;
    }

    public static User getById(String id){
        return userMapper.selectById(id);
    }

    public static List<User> getListByIds(String ids) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime)
                .in(User::getId,ids.split(","));
        return userMapper.selectList(wrapper);
    }

    public static String getRoleNameById(String id){
        return "testRole";
    }

    public static String getDeptNameById(String id){
        return "testRole";
    }

}

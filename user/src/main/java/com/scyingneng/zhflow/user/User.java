package com.scyingneng.zhflow.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.scyingneng.zhflow.entity.BaseEntity;
import com.scyingneng.zhflow.vo.SelectOutVO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void addUser(String userName,String password) throws Exception {
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new Exception("用户名或密码不能为空");
        }

        if(getByUserName(userName)!=null){
            throw new Exception("用户名已存在");
        }

        User user=new User();
        user.setBase(true);
        user.setUserName(userName);
        user.setPassword(password);

        userMapper.insert(user);
    }

    public static void updatePassword(String id, String password) throws Exception {
        if(StringUtils.isEmpty(password)){
            throw new Exception("密码不能为空");
        }

        User user = getById(id);
        if(user==null){
            throw new Exception("用户不已存在");
        }

        if(user.getUserName().equals("admin"))return ;

        user.setPassword(password);
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
    }

    public static void del(String id) {
        User user = getById(id);
        if(user!=null && user.getUserName().equals("admin"))return ;

        userMapper.deleteById(id);
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

    public static List<SelectOutVO> getListToSelect(String name) {
        List<User> list=getListNoPage(name);

        List<SelectOutVO> outList=new ArrayList<>();
        for (User user:list){
            outList.add(new SelectOutVO(user.getId(),user.getUserName()));
        }

        return outList;
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

    public static String getNameListByIds(String ids) {
        List<User> list=getListByIds(ids);
        return list.stream().map(User::getUserName).collect(Collectors.joining(","));
    }
}

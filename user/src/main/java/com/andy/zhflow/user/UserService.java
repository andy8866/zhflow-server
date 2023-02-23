package com.andy.zhflow.user;

import com.alibaba.fastjson.JSON;
import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.SecurityUser;
import com.andy.zhflow.security.config.SecurityStarterAutoConfigure;
import com.andy.zhflow.security.utils.UserUtil;
import com.andy.zhflow.service.user.IUserService;
import com.andy.zhflow.service.user.UserOutVO;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class UserService implements IUserService {

    @Resource
    private RedisService redisService;

    public User getCurrentUser() {
        String id= UserUtil.getUserId();
        return User.getById(id);
    }

    public String switchCurrentUser(String id) {
        User user=User.getById(id);
        SecurityUser securityUser = new SecurityUser(user.getId(), user.getUserName(), user.getPassword(), "ROLE_admin");

        // 创建令牌
        String token = NanoIdUtils.randomNanoId();
        String json= JSON.toJSONString(securityUser);
        redisService.set(token,json, SecurityStarterAutoConfigure.EXPIRATION, TimeUnit.SECONDS);

        return token;
    }

    /**
     * 获取上级用户
     * @param userId
     * @return
     */
    public String getSuperiorUser(String userId) {
        return "Qsw0NCe5n1RxA5Xh1c-td";
    }

    @Override
    public UserOutVO getByUserName(String userName) {
        User user = User.getByUserName(userName);
        UserOutVO outVO=new UserOutVO();
        BeanUtils.copyProperties(user,outVO);
        return outVO;
    }
}

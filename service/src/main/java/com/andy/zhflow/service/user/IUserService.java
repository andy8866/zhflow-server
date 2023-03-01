package com.andy.zhflow.service.user;

public interface IUserService {
    UserOutVO getByUserName(String userName);


    String getSuperiorUserId(String userId);

    String getNameById(String id);
}

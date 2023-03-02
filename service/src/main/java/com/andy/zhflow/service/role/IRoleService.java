package com.andy.zhflow.service.role;

import java.util.List;

public interface IRoleService {



    List<String> getUserRoleIds(String userId);

    String getRoleNameById(String roleId);

    List<String> getUserIdsByRoleId(String roleId);
}

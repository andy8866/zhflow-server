package com.scyingneng.zhflow.service.dept;

import java.util.List;

public interface IDeptService {

    List<String> getUserDeptIds(String userId);

    String getDeptNameById(String deptId) ;

    List<String> getUserIdsByDeptId(String deptId);

}

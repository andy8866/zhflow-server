package com.scyingneng.zhflow.service.thirdApp;

import com.alibaba.fastjson.TypeReference;
import com.scyingneng.zhflow.vo.SelectOutVO;

import java.util.List;
import java.util.Map;

public interface IThirdAppService {
    <T> T callApi(String appId, String code, Map<String, Object> params) throws Exception;
    <T> T callApi(String appId, String code, Map<String, Object> params,TypeReference typeReference) throws Exception;

    String getSuperiorUserId(String appId,String userId) throws Exception;
    String getUserNameById(String appId,String userId) throws Exception;
    List<SelectOutVO> getDeptListToSelect(String appId) throws Exception;

    List<SelectOutVO> getRoleListToSelect(String appId) throws Exception;
    List<SelectOutVO> getUserListToSelect(String appId) throws Exception;

    Map<String,String> getDictValueMap(String appId, String type)  throws Exception;
    void taskComplete(String appId, Map<String,Object> params) throws Exception;
    void procComplete(String appId, Map<String,Object> params) throws Exception;

    void startProc(String appId, Map<String,Object>  variableMap)throws Exception;

    List<String> getUserIdsByRoleId(String appId, String roleId)throws Exception ;

    List<String> getUserIdsByDeptId(String appId, String deptId)throws Exception ;
}

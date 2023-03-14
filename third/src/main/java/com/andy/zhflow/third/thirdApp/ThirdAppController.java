package com.andy.zhflow.third.thirdApp;

import com.andy.zhflow.response.ResultResponse;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.service.thirdApp.IThirdAppService;
import com.andy.zhflow.vo.SelectOutVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 调用第三方APP，签名验证
 */
@RestController()
@RequestMapping(value = "/api/thirdApp")
public class ThirdAppController {

    @Autowired
    private IThirdAppService thirdAppService;

    @Autowired
    private IAuthService authService;

    /**
     * 提供给页面调用
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value="/call")
    public ResultResponse<Object> call(@RequestBody() Map<String,Object> params) throws Exception {

        String code= (String) params.get("code");
        if(StringUtils.isEmpty(code)) return ResultResponse.fail("缺少code");

        Object result = thirdAppService.callApi(authService.getAppId(), code, params);
        return ResultResponse.success(result);
    }

    @PostMapping(value="/getSuperiorUserId")
    public ResultResponse<String> getSuperiorUserId(@RequestBody Map<String,String> map) throws Exception {
        String userId=map.getOrDefault("userId",null);
        if(StringUtils.isEmpty(userId)) throw new Exception("缺少用户ID");
        return ResultResponse.success( thirdAppService.getSuperiorUserId(authService.getAppId(),userId));
    }

    @PostMapping(value="/getUserNameById")
    public ResultResponse<String> getUserNameById(@RequestBody Map<String,String> map) throws Exception {
        String userId=map.getOrDefault("userId",null);
        if(StringUtils.isEmpty(userId)) throw new Exception("缺少用户ID");
        return ResultResponse.success( thirdAppService.getUserNameById(authService.getAppId(),userId));
    }

    @PostMapping(value="/getDeptListToSelect")
    public ResultResponse<List<SelectOutVO>> getDeptListToSelect() throws Exception {
        return ResultResponse.success( thirdAppService.getDeptListToSelect(authService.getAppId()));
    }

    @PostMapping(value="/getRoleListToSelect")
    public ResultResponse<List<SelectOutVO>> getRoleListToSelect() throws Exception {
        return ResultResponse.success( thirdAppService.getRoleListToSelect(authService.getAppId()));
    }

    @PostMapping(value="/getUserListToSelect")
    public ResultResponse<List<SelectOutVO>> getUserListToSelect() throws Exception {
        return ResultResponse.success( thirdAppService.getUserListToSelect(authService.getAppId()));
    }

    @PostMapping(value="/getDicValueMap")
    public ResultResponse<Map<String,String>> getDicValueMap(@RequestBody Map<String,String> map) throws Exception {
        String type=map.getOrDefault("type",null);
        return ResultResponse.success( thirdAppService.getDicValueMap(authService.getAppId(),type));
    }
}

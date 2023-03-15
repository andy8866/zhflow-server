package com.scyingneng.zhflow.feign.user;

import com.scyingneng.zhflow.feign.FeignFallBackFactory;
import com.scyingneng.zhflow.feign.FeignHeaderConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user",url = "${feign.url}/api/user",configuration = FeignHeaderConfig.class,fallbackFactory = FeignFallBackFactory.class)
public interface UserFeign {
//    @GetMapping("/getByUserName")
//    public ResultResponse<UserOutVO> getByUserName(@RequestParam("userName") String userName) throws Exception;
}

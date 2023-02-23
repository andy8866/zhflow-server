package com.andy.zhflow.feign;

/**
 * 服务降级处理类
 */
public class FeignFallBackImp {

    String errMsg;

    public FeignFallBackImp(String errMsg) {
        this.errMsg = errMsg;
    }
}
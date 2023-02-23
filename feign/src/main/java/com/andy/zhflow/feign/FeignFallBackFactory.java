package com.andy.zhflow.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignFallBackFactory<T> implements FallbackFactory<T>{
    @Override
    public T create(Throwable cause) {
        if (cause.getMessage() != null)
            log.error("feign接口调用异常,{}", cause.getMessage());
        return (T) new FeignFallBackImp(cause.getMessage());
    }
}

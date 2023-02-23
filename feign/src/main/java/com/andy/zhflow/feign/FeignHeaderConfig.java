package com.andy.zhflow.feign;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHeaderConfig {

    /**
     * feign请求拦截器
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }

    /**
     * 开启feign调用日志打印的debug模式
     *
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

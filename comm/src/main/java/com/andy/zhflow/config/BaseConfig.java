package com.andy.zhflow.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("base-config")
public class BaseConfig {
    // 过期时间是3600秒，既是1个小时
    public static long EXPIRATION = 24*60 * 60L;

    private long expire=EXPIRATION;

    public void setExpire(long expire) {
        this.expire = expire;
        BaseConfig.EXPIRATION=expire;
    }
}

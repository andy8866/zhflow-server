package com.andy.zhflow.security.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("security")
public class SecurityConfig {

    public List<String> permits=new ArrayList<>();

    private long expiration=24*60 * 60L;
}

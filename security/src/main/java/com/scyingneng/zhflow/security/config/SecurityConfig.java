package com.scyingneng.zhflow.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("security")
public class SecurityConfig {

    public List<String> permits=new ArrayList<>();
}

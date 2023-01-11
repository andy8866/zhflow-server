package com.andy.zhflow.mysql.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.andy.zhflow.*")
public class MysqlStarterAutoConfigure {
}

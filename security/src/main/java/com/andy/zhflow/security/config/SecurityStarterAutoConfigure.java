package com.andy.zhflow.security.config;

import com.andy.zhflow.redis.service.RedisService;
import com.andy.zhflow.security.handler.LoginFailureHandler;
import com.andy.zhflow.security.handler.SecurityAuthenticationEntryPoint;
import com.andy.zhflow.security.token.TokenAuthorizationFilter;
import com.andy.zhflow.security.token.TokenLoginSuccessHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityStarterAutoConfigure {

    private SecurityConfig securityConfig;
    @Autowired
    public void setSecurityConfig(SecurityConfig securityConfig){
        this.securityConfig=securityConfig;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager loadAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // configure SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManager authenticationManager,
                                           RedisService redisService) throws Exception {
        http.csrf().disable();

        http.cors().configurationSource(corsConfigurationSource());

        http.authorizeHttpRequests((authorize) -> {
            for (String permit:securityConfig.getPermits()){
                authorize.antMatchers(permit).permitAll();
            }
            authorize.antMatchers("/api/frame/**","/api/admin/dict/**").hasAnyRole("app");
            authorize.antMatchers("/**").hasAnyRole("admin", "user");
        });

        http.exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint());

        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter=new UsernamePasswordAuthenticationFilter(authenticationManager);
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/api/login");
//        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new TokenLoginSuccessHandler(redisService));
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        http.addFilterAt(usernamePasswordAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

//        JwtAuthorizationFilter authorizationFilter=new JwtAuthorizationFilter(authenticationManager,new SecurityAuthenticationEntryPoint());
//        http.addFilterAt(authorizationFilter, BasicAuthenticationFilter.class);

        TokenAuthorizationFilter authorizationFilter=new TokenAuthorizationFilter(authenticationManager,
                new SecurityAuthenticationEntryPoint(),
                redisService);
        http.addFilterAt(authorizationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        // Very permissive CORS config...
        final var configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
//        configuration.addAllowedOrigin("*");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public MyErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                                  ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        serverProperties.getError().setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        return new MyErrorController(errorAttributes, serverProperties.getError(), errorViewResolversProvider.getIfAvailable());
    }

}

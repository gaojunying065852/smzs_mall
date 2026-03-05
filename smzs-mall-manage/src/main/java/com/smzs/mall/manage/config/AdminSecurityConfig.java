package com.smzs.mall.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.core.annotation.Order;

/**
 * 管理后台安全配置
 */
@Configuration
@EnableWebSecurity
@Order(200)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/admin/**")
            .authorizeRequests()
                .antMatchers("/api/admin/login", "/api/admin/test/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement()
            .maximumSessions(1);
    }

    /**
     * 管理后台密码加密器
     */
    @Bean
    public PasswordEncoder adminPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
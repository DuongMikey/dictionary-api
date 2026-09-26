package com.mikey.dictionary.config;

import com.mikey.dictionary.filter.AdminAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${dictionary.admin.secret-key}")
    private String adminSecret;

    @Bean
    public FilterRegistrationBean<AdminAuthFilter> adminFilterRegistration() {
        FilterRegistrationBean<AdminAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminAuthFilter(adminSecret));
        registrationBean.addUrlPatterns("/api/admin/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
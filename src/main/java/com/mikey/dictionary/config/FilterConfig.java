package com.mikey.dictionary.config;

import com.mikey.dictionary.filter.AdminAuthFilter;
import com.mikey.dictionary.filter.ApiKeyAuthFilter;
import com.mikey.dictionary.service.ApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class FilterConfig {

    private final HandlerExceptionResolver resolver;


    public FilterConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Bean
    public FilterRegistrationBean<AdminAuthFilter> adminFilterRegistration(
            @Value("${dictionary.admin.secret-key}") String adminSecret
    ) {
        FilterRegistrationBean<AdminAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminAuthFilter(adminSecret, this.resolver));
        registrationBean.addUrlPatterns("/v3/api/admin/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyAuthFilterRegistration(
            ApiService service
    ) {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiKeyAuthFilter(this.resolver, service));
        registrationBean.addUrlPatterns("/v3/api/dictionaries/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}

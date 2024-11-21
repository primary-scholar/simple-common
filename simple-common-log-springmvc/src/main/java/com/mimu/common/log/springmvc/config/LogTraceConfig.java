package com.mimu.common.log.springmvc.config;

import com.mimu.common.log.springmvc.filter.LogTraceFilter;
import com.mimu.common.log.springmvc.interceptor.LogTraceInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LogTraceConfig {

    @Bean
    public WebMvcConfigurer customLogInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LogTraceInterceptor()).addPathPatterns("/**");
            }
        };
    }

    @Bean
    public FilterRegistrationBean<LogTraceFilter> customLogFilter() {
        FilterRegistrationBean<LogTraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogTraceFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}

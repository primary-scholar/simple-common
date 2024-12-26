package com.mimu.common.log.springmvc.test.application.config;

import com.mimu.common.log.http.CommonHttpClient;
import com.mimu.common.log.http.PoolingHttpConnectionManagerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonHttpClientConfig {

    @Bean
    public CommonHttpClient commonHttpClient() {
        PoolingHttpConnectionManagerConfig poolingHttpConnectionManagerConfig =
                new PoolingHttpConnectionManagerConfig();
        return new CommonHttpClient(poolingHttpConnectionManagerConfig);
    }
}

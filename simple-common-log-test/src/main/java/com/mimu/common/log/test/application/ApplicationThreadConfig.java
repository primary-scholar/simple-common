package com.mimu.common.log.test.application;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationThreadConfig {

    @Bean
    public ExecutorService executorService() {
        return TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));
    }
}

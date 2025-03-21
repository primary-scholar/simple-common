package com.mimu.common.log.dubbo.consumer.test.config;

import com.mimu.common.log.dubbo.consumer.test.service.CalculateConsumerService;
import com.mimu.common.log.springmvc.config.LogTraceConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LogTraceConfig.class)
@EnableDubbo(scanBasePackageClasses = CalculateConsumerService.class)
public class CalculateConsumerApplicationConfig {

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("calculate-consumer-application");
        applicationConfig.setQosEnable(Boolean.FALSE);
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("127.0.0.1:2181");
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        protocolConfig.setThreads(10);
        protocolConfig.setDispatcher("message");
        return protocolConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setRegistry(registryConfig());
        consumerConfig.setCheck(Boolean.FALSE);
        consumerConfig.setRetries(0);
        return consumerConfig;
    }
}

package com.mimu.common.log.dubbo.provider.test.config;

import com.mimu.common.log.dubbo.provider.test.service.CalculateEchoServiceImpl;
import com.mimu.common.log.springmvc.config.LogTraceConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LogTraceConfig.class)
@EnableDubbo(scanBasePackageClasses = CalculateEchoServiceImpl.class)
public class CalculateProviderApplicationConfig {

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("calculate-provider-application");
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
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setRegistry(registryConfig());
        providerConfig.setRegister(Boolean.TRUE);
        providerConfig.setTimeout(3000);
        return providerConfig;
    }
}

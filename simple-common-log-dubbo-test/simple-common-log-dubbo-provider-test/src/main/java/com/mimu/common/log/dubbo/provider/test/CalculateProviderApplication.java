package com.mimu.common.log.dubbo.provider.test;

import com.mimu.common.log.dubbo.provider.test.config.CalculateProviderApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CalculateProviderApplicationConfig.class)
public class CalculateProviderApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(CalculateProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CalculateProviderApplication.class, args);
        LOGGER.info("dubbo provider application start success");
    }
}

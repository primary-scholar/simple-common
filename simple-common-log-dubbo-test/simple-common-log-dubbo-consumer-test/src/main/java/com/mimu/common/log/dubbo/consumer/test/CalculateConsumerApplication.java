package com.mimu.common.log.dubbo.consumer.test;

import com.mimu.common.log.dubbo.consumer.test.config.CalculateConsumerApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CalculateConsumerApplicationConfig.class)
public class CalculateConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculateConsumerApplication.class, args);
    }
}

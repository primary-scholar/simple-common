package com.mimu.common.log.dubbo.consumer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalculateConsumerApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(CalculateConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CalculateConsumerApplication.class, args);
        LOGGER.info("dubbo consumer application start success");
    }
}

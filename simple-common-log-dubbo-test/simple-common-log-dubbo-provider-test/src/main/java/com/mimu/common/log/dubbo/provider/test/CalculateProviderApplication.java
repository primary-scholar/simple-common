package com.mimu.common.log.dubbo.provider.test;

import com.mimu.common.log.dubbo.provider.test.config.CalculateProviderApplicationConfig;
import org.apache.dubbo.container.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;

@Import(CalculateProviderApplicationConfig.class)
public class CalculateProviderApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(CalculateProviderApplication.class);

    public static void main(String[] args) {
        LOGGER.info("start dubbo application");
        Main.main(args);
        LOGGER.info("start dubbo application end");
    }
}

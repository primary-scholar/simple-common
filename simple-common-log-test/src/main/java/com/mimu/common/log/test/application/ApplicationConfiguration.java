package com.mimu.common.log.test.application;

import com.mimu.common.log.springmvc.config.LogTraceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LogTraceConfig.class)
public class ApplicationConfiguration {
}

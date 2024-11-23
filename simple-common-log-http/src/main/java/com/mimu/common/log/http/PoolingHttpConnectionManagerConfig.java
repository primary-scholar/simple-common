package com.mimu.common.log.http;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PoolingHttpConnectionManagerConfig {
    private Integer maxTotal = 50;
    private Integer maxPerRoute = 50;
    private Integer connectionTimeout = 3000;
    private Integer validConnectionInterval = 3000;
}

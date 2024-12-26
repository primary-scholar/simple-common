package com.mimu.common.log.dubbo.test.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CalculateEchoRequest implements Serializable {
    private Integer result;
}

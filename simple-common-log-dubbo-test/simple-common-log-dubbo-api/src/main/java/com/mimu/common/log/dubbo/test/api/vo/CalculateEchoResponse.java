package com.mimu.common.log.dubbo.test.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CalculateEchoResponse implements Serializable {
    private Integer result;
    private String message;
}

package com.mimu.common.log.springmvc.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class CalculateResult implements Serializable {
    private Integer result;
    private String message;
}
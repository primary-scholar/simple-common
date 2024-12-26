package com.mimu.common.log.springmvc.test.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class NumberOperationParam extends BaseParam implements Serializable {
    /**
     * 待计算的数字
     */
    private Integer number;

    /**
     * 操作
     */
    private Integer operation;
}

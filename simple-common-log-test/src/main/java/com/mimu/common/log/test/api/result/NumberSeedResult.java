package com.mimu.common.log.test.api.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class NumberSeedResult implements Serializable {
    /**
     * 根据种子获取的数字
     */
    private Integer number;
}

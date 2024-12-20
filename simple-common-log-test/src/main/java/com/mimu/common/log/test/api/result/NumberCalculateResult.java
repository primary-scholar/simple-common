package com.mimu.common.log.test.api.result;

import com.mimu.common.log.test.api.param.BaseParam;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class NumberCalculateResult extends BaseParam implements Serializable {
    /**
     *
     */
    private Integer operation;
    /**
     *
     */
    private String operationDesc;
    /**
     *
     */
    private NumberSeedResult first;
    /**
     *
     */
    private NumberSeedResult second;
    /**
     *
     */
    private Integer result;
}

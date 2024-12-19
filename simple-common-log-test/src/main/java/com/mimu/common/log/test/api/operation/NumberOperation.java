package com.mimu.common.log.test.api.operation;


import com.mimu.common.log.test.api.result.NumberSeedResult;

public interface NumberOperation {

    Integer getOperation();

    String getOperationDesc();

    Integer operate(NumberSeedResult first, NumberSeedResult second);

}

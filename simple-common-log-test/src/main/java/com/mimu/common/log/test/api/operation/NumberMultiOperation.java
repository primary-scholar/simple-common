package com.mimu.common.log.test.api.operation;

import com.mimu.common.log.test.api.result.NumberSeedResult;
import org.apache.commons.lang3.math.NumberUtils;

public class NumberMultiOperation implements NumberOperation {

    @Override
    public Integer getOperation() {
        return NumberUtils.INTEGER_TWO;
    }

    @Override
    public String getOperationDesc() {
        return "乘法";
    }

    @Override
    public Integer operate(NumberSeedResult first, NumberSeedResult second) {
        return first.getNumber() * second.getNumber();
    }
}

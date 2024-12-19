package com.mimu.common.log.test.application.utils;


import com.mimu.common.log.test.api.operation.NumberOperation;
import com.mimu.common.log.test.api.operation.NumberOperationSelector;
import com.mimu.common.log.test.api.param.NumberCalculateParam;
import com.mimu.common.log.test.api.param.NumberOperationParam;
import com.mimu.common.log.test.api.result.NumberCalculateResult;
import com.mimu.common.log.test.api.result.NumberSeedResult;

public class NumberOperationBuilder {

    public static NumberSeedResult build(NumberOperationParam param) {
        NumberSeedResult seedResult = new NumberSeedResult();
        seedResult.setNumber(param.getNumber());
        return seedResult;
    }

    public static NumberCalculateResult build(NumberCalculateParam param) {
        Integer operation = param.getOperation();
        NumberOperation select = NumberOperationSelector.select(operation);
        NumberSeedResult first = param.getFirst();
        NumberSeedResult second = param.getSecond();
        Integer operate = select.operate(first, second);
        NumberCalculateResult result = new NumberCalculateResult();
        result.setOperation(operation);
        result.setFirst(first);
        result.setSecond(second);
        result.setResult(operate);
        return result;
    }

    public static NumberCalculateParam build(NumberSeedResult first, NumberSeedResult second, Integer operation) {
        NumberCalculateParam param = new NumberCalculateParam();
        param.setFirst(first);
        param.setSecond(second);
        param.setOperation(operation);
        return param;
    }

}

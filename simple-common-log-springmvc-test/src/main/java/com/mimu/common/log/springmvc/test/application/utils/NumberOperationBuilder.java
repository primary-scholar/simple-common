package com.mimu.common.log.springmvc.test.application.utils;


import com.mimu.common.log.springmvc.test.api.operation.NumberOperation;
import com.mimu.common.log.springmvc.test.api.operation.NumberOperationSelector;
import com.mimu.common.log.springmvc.test.api.param.NumberCalculateParam;
import com.mimu.common.log.springmvc.test.api.param.NumberOperationParam;
import com.mimu.common.log.springmvc.test.api.result.NumberCalculateResult;
import com.mimu.common.log.springmvc.test.api.result.NumberSeedResult;

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
        result.setP1(param.getP1());
        result.setOperation(operation);
        result.setOperationDesc(select.getOperationDesc());
        result.setFirst(first);
        result.setSecond(second);
        result.setResult(operate);
        return result;
    }

    public static NumberCalculateParam build(NumberSeedResult first, NumberSeedResult second,
                                             NumberOperationParam operationParam) {
        NumberCalculateParam calculateParam = new NumberCalculateParam();
        calculateParam.setP1(operationParam.getP1());
        calculateParam.setFirst(first);
        calculateParam.setSecond(second);
        calculateParam.setOperation(operationParam.getOperation());
        return calculateParam;
    }

}

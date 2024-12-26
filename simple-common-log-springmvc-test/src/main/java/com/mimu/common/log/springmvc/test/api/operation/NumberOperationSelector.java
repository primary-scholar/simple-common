package com.mimu.common.log.springmvc.test.api.operation;

import org.apache.commons.lang3.math.NumberUtils;

public class NumberOperationSelector {

    public static NumberOperation select(Integer operation) {
        if (operation.equals(NumberUtils.INTEGER_ONE)) {
            return new NumberAddOperation();
        }
        if (operation.equals(NumberUtils.INTEGER_TWO)) {
            return new NumberMultiOperation();
        }
        throw new RuntimeException("Invalid operation");
    }
}

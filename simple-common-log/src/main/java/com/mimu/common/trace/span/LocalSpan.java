package com.mimu.common.trace.span;


public class LocalSpan extends TraceSpan {

    public LocalSpan(Integer parentSpanId, Integer spanId, String spanName) {
        super(parentSpanId, spanId, spanName);
    }

    @Override
    public Boolean isEntry() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isExit() {
        return Boolean.FALSE;
    }
}

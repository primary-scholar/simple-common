package com.mimu.common.trace.span;

public class ExitSpan extends TraceSpan {
    public ExitSpan(Integer parentId, String spanName, Integer spanId) {
        super(parentId, spanName, spanId);
    }
}

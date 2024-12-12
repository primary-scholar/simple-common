package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class EntrySpan extends TraceSpan {
    public EntrySpan(Integer parentSpanId, String spanName, Integer spanId, Tracer tracer) {
        super(parentSpanId, spanName, spanId, tracer);
    }

    @Override
    public Boolean isEntry() {
        return Boolean.TRUE;
    }

    @Override
    public Boolean isExit() {
        return Boolean.FALSE;
    }
}

package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class ExitSpan extends TraceSpan {
    public ExitSpan(Integer parentSpanId, String spanName, Integer spanId, Tracer tracer) {
        super(parentSpanId, spanName, spanId, tracer);
    }


    @Override
    public Boolean isEntry() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isExit() {
        return Boolean.TRUE;
    }
}

package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class ExitSpan extends TraceSpan {
    public ExitSpan(Integer parentId, String spanName, Integer spanId, Tracer tracer) {
        super(parentId, spanName, spanId, tracer);
    }
}

package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class EntrySpan extends TraceSpan {
    public EntrySpan(Integer parentId, String spanName, Integer spanId, Tracer tracer) {
        super(parentId, spanName, spanId, tracer);
    }
}

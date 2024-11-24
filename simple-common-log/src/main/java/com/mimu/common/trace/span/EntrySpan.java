package com.mimu.common.trace.span;

public class EntrySpan extends TraceSpan {
    public EntrySpan(Integer parentId, String spanName, Integer spanId) {
        super(parentId, spanName, spanId);
    }
}

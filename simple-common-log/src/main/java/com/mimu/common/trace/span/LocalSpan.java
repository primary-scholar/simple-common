package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class LocalSpan extends TraceSpan {

    public LocalSpan(Tracer tracer, Integer parentSpanSequenceId, Integer spanSequenceId, String spanName) {
        super(tracer, parentSpanSequenceId, spanSequenceId, spanName);
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

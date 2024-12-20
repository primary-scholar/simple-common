package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class EntrySpan extends TraceSpan {
    public EntrySpan(Tracer tracer, Integer parentSequenceId, Integer spanSequenceId, String spanName) {
        super(tracer, parentSequenceId, spanSequenceId, spanName);
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

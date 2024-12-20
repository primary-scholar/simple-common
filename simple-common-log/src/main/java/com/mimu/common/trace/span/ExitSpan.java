package com.mimu.common.trace.span;

import com.mimu.common.trace.Tracer;

public class ExitSpan extends TraceSpan {
    public ExitSpan(Tracer tracer, Integer parentSequenceId, Integer spanSequenceId, String spanName) {
        super(tracer, parentSequenceId, spanSequenceId, spanName);
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

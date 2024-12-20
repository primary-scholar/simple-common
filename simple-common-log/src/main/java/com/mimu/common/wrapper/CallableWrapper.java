package com.mimu.common.wrapper;

import com.mimu.common.trace.context.TraceContextSnapshot;

import java.util.concurrent.Callable;

public class CallableWrapper implements Callable {

    private Callable callable;
    private TraceContextSnapshot traceContextSnapshot;

    @Override
    public Object call() throws Exception {
        return null;
    }
}

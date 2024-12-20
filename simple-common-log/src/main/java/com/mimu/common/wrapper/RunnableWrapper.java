package com.mimu.common.wrapper;

import com.mimu.common.trace.context.TraceContextSnapshot;

public class RunnableWrapper implements Runnable {
    private Runnable runnable;
    private TraceContextSnapshot contextSnapshot;

    @Override
    public void run() {

    }
}

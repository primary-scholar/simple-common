package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

public class RunnableWrapper implements Runnable {
    private final Runnable runnable;
    private final TraceContextSnapshot contextSnapshot;

    public RunnableWrapper(Runnable runnable, TraceContextSnapshot contextSnapshot) {
        this.runnable = runnable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public void run() {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            runnable.run();
        } finally {
            ContextManager.stopSpan();
        }
    }
}

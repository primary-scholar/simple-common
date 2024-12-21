package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

public class CallableWrapper<T> implements Callable<T> {

    private final Callable<T> callable;
    private final TraceContextSnapshot contextSnapshot;

    public CallableWrapper(Callable<T> callable, TraceContextSnapshot contextSnapshot) {
        this.callable = callable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public T call() throws Exception {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return callable.call();
        } finally {
            ContextManager.stopSpan();
        }
    }
}

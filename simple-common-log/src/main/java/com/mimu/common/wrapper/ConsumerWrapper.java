package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class ConsumerWrapper<T> implements Consumer<T> {

    private final Consumer<T> consumer;
    private final TraceContextSnapshot contextSnapshot;

    public ConsumerWrapper(Consumer<T> consumer, TraceContextSnapshot contextSnapshot) {
        this.consumer = consumer;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public void accept(T t) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            consumer.accept(t);
        } finally {
            ContextManager.stopSpan();
        }
    }
}

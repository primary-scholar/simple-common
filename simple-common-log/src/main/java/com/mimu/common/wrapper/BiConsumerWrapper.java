package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiConsumer;

public class BiConsumerWrapper<T, U> implements BiConsumer<T, U> {

    private final BiConsumer<T, U> biConsumer;
    private final TraceContextSnapshot contextSnapshot;

    public BiConsumerWrapper(BiConsumer<T, U> biConsumer, TraceContextSnapshot contextSnapshot) {
        this.biConsumer = biConsumer;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public void accept(T t, U u) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            biConsumer.accept(t, u);
        } finally {
            ContextManager.stopSpan();
        }
    }

    @Override
    public BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return biConsumer.andThen(after);
        } finally {
            ContextManager.stopSpan();
        }
    }
}

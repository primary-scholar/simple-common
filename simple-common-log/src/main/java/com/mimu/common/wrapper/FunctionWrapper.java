package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public class FunctionWrapper<T, R> implements Function<T, R> {

    private Function<T, R> function;
    private TraceContextSnapshot contextSnapshot;

    public FunctionWrapper(Function<T, R> function, TraceContextSnapshot contextSnapshot) {
        this.function = function;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public R apply(T t) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return function.apply(t);
        } finally {
            ContextManager.stopSpan();
        }
    }

    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return function.compose(before);
        } finally {
            ContextManager.stopSpan();
        }

    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return function.andThen(after);
        } finally {
            ContextManager.stopSpan();
        }
    }
}

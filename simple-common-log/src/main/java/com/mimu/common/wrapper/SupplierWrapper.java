package com.mimu.common.wrapper;

import com.mimu.common.trace.context.TraceContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class SupplierWrapper<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private final TraceContextSnapshot contextSnapshot;

    public SupplierWrapper(Supplier<T> supplier, TraceContextSnapshot contextSnapshot) {
        this.supplier = supplier;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public T get() {
        TraceSpan localSpan = TraceContextManager.createLocalSpan(StringUtils.EMPTY);
        TraceContextManager.continued(contextSnapshot);
        try {
            return supplier.get();
        } finally {
            TraceContextManager.stopSpan();
        }
    }
}

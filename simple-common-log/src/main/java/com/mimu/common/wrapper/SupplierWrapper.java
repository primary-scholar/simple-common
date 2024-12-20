package com.mimu.common.wrapper;

import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class SupplierWrapper<T> implements Supplier<T> {

    private Supplier<T> supplier;
    private TraceContextSnapshot contextSnapshot;

    public SupplierWrapper(Supplier supplier, TraceContextSnapshot contextSnapshot) {
        this.supplier = supplier;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public T get() {
        TraceSpan localSpan = ContextManager.createLocalSpan(StringUtils.EMPTY);
        ContextManager.continued(contextSnapshot);
        try {
            return supplier.get();
        } finally {
            ContextManager.stopSpan();
        }
    }
}

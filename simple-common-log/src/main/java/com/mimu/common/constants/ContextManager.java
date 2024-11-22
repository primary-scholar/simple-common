package com.mimu.common.constants;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Objects;

public class ContextManager {

    private static final TransmittableThreadLocal<TraceContext> CONTEXT = new TransmittableThreadLocal<>();

    private static TraceContext getOrCreate() {
        TraceContext context = CONTEXT.get();
        if (Objects.isNull(context)) {
            context = new TraceContext();
            CONTEXT.set(context);
        }
        return context;
    }

    public static TraceContext getContext() {
        return getOrCreate();
    }

}

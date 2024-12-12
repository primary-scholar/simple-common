package com.mimu.common.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.mimu.common.trace.span.TraceSpan;

import java.util.Objects;

public class ContextManager {

    private static final TransmittableThreadLocal<TraceContext> CONTEXT = new TransmittableThreadLocal<>();

    private static TraceContext getOrCreateContext() {
        TraceContext context = CONTEXT.get();
        if (Objects.isNull(context)) {
            context = new TraceContext();
            CONTEXT.set(context);
        }
        return context;
    }

    public static TraceContext get() {
        return CONTEXT.get();
    }

    public static TraceSpan createEntrySpan(String operationName, ContextCarrier carrier) {
        TraceContext context = getOrCreateContext();
        TraceSpan entrySpan = context.createEntrySpan(operationName);
        if (Objects.nonNull(carrier) && carrier.isValid()) {
            context.extract(carrier);
        }
        return entrySpan;
    }

    public static TraceSpan createExitSpan(String operationName, ContextCarrier carrier, String peer) {
        TraceContext context = get();
        if (Objects.isNull(context)) {
            throw new IllegalStateException("context is null");
        }
        TraceSpan entrySpan = context.createExitSpan(operationName, peer);
        context.inject(carrier);
        return entrySpan;
    }

    public static TraceSpan activeSpan() {
        return get().activeSpan();
    }

    public static void stopSpan() {
        TraceContext traceContext = get();
        stopSpan(traceContext.activeSpan(), traceContext);
    }

    private static void stopSpan(TraceSpan span, TraceContext context) {
        if (context.stopSpan(span)) {
            CONTEXT.remove();
        }
    }

}

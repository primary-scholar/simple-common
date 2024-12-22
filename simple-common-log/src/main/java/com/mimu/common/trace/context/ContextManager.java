package com.mimu.common.trace.context;

import com.mimu.common.trace.id.DistributedId;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ContextManager {

    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();

    private static TraceContext getOrCreateContext() {
        TraceContext context = CONTEXT.get();
        if (Objects.isNull(context)) {
            context = new TraceContext();
            CONTEXT.set(context);
        }
        return context;
    }

    private static TraceContext get() {
        return CONTEXT.get();
    }

    public static DistributedId createDistributedId(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return new DistributedId(id);
    }

    public static TraceSpan createEntrySpan(String operationName, ContextCarrier carrier) {
        TraceContext context = getOrCreateContext();
        TraceSpan entrySpan = context.createEntrySpan(operationName);
        if (Objects.nonNull(carrier) && carrier.isValid()) {
            context.extract(carrier, entrySpan);
        }
        return entrySpan;
    }

    public static TraceSpan createExitSpan(String operationName, ContextCarrier carrier, String peer) {
        TraceContext context = getOrCreateContext();
        if (Objects.isNull(context)) {
            throw new IllegalStateException("context is null");
        }
        TraceSpan exitSpan = context.createExitSpan(operationName, peer);
        context.inject(carrier, exitSpan);
        return exitSpan;
    }

    public static TraceSpan createLocalSpan(String operationName) {
        TraceContext context = getOrCreateContext();
        return context.createLocalSpan(operationName);
    }

    public static TraceContextSnapshot capture() {
        return get().capture();
    }

    public static void continued(TraceContextSnapshot contextSnapshot) {
        if (Objects.isNull(contextSnapshot)) {
            throw new IllegalStateException("context snapshot is null");
        }
        if (!contextSnapshot.isFromCurrent()) {
            if (contextSnapshot.isValid()) {
                get().continued(contextSnapshot);
            }
        }
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

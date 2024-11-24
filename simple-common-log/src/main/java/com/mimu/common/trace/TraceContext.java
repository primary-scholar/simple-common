package com.mimu.common.trace;

import com.mimu.common.trace.span.TraceSpan;

import java.util.Objects;

public class TraceContext {

    private Tracer tracer;

    public TraceContext() {
        this.tracer = new Tracer();
    }

    public String getTraceId() {
        return tracer.getTraceId();
    }

    public void setTraceId(String requestId) {
        tracer.setTraceId(requestId);
    }

    public TraceSpan createEntrySpan(String operationName) {
        TraceSpan peek = this.tracer.peek();
        if (Objects.isNull(peek)) {

        }
        return null;
    }

    public void extract(ContextCarrier carrier) {

    }

    public TraceSpan createExitSpan(String operationName, String peer) {
        return null;
    }

    public void inject(ContextCarrier carrier) {

    }

    public TraceSpan activeSpan() {
        TraceSpan span = tracer.peek();
        if (Objects.isNull(span)) {
            throw new IllegalStateException("No active span");
        }
        return span;
    }

    public void stopSpan(TraceSpan span) {

    }
}

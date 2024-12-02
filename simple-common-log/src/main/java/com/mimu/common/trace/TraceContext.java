package com.mimu.common.trace;

import com.mimu.common.trace.span.EntrySpan;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TraceContext {

    private Tracer tracer;
    private Integer spanIdGenerator;
    private List<TraceSpan> activeSpans = new LinkedList<>();

    public TraceContext() {
        this.tracer = new Tracer();
        this.spanIdGenerator = 0;
    }

    public String getTraceId() {
        return tracer.getTraceId();
    }

    public void setTraceId(String requestId) {
        tracer.setTraceId(requestId);
    }

    public TraceSpan createEntrySpan(String operationName) {
        TraceSpan entrySpan;
        TraceSpan parentSpan = peek();
        Integer parentSpanId = Objects.isNull(parentSpan) ? NumberUtils.INTEGER_MINUS_ONE : parentSpan.getParentId();
        if (Objects.isNull(parentSpan)) {
            entrySpan = new EntrySpan(parentSpanId++, operationName, spanIdGenerator++, tracer);
            entrySpan.start();
            return push(entrySpan);
        } else {
            parentSpan.setSpanName(operationName);
            entrySpan = parentSpan;
            return entrySpan.start();
        }
    }

    public void extract(ContextCarrier carrier) {
        this.tracer.setTraceId(carrier.getTraceId());
    }

    public TraceSpan createExitSpan(String operationName, String peer) {
        return null;
    }

    public void inject(ContextCarrier carrier) {

    }

    public TraceSpan activeSpan() {
        TraceSpan span = peek();
        if (Objects.isNull(span)) {
            throw new IllegalStateException("No active span");
        }
        return span;
    }

    public void stopSpan(TraceSpan span) {

    }

    private TraceSpan push(TraceSpan span) {
        activeSpans.add(span);
        return span;
    }

    private TraceSpan peek() {
        if (activeSpans.isEmpty()) {
            return null;
        }
        return activeSpans.get(activeSpans.size() - 1);
    }
}

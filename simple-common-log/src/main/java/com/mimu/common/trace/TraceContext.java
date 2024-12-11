package com.mimu.common.trace;

import com.mimu.common.trace.span.EntrySpan;
import com.mimu.common.trace.span.ExitSpan;
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
        Tracer currentTracer = this.tracer;
        if (Objects.nonNull(parentSpan) && parentSpan.isEntry()) {
            parentSpan.setSpanName(operationName);
            entrySpan = parentSpan;
            return entrySpan.start();
        } else {
            Integer parentSpanId = Objects.isNull(parentSpan) ? NumberUtils.INTEGER_MINUS_ONE :
                    parentSpan.getParentId();
            entrySpan = new EntrySpan(parentSpanId++, operationName, spanIdGenerator++, currentTracer);
            entrySpan.start();
            return push(entrySpan);
        }
    }

    public void extract(ContextCarrier carrier) {
        this.tracer.setTraceId(carrier.getTraceId());
    }

    public TraceSpan createExitSpan(String operationName, String peer) {
        TraceSpan exitSpan;
        TraceSpan parentSpan = peek();
        Tracer currentTracer = this.tracer;
        if (Objects.nonNull(parentSpan) && parentSpan.isExit()) {
            exitSpan = parentSpan;
        } else {
            Integer parentSpanId = Objects.isNull(parentSpan) ? NumberUtils.INTEGER_MINUS_ONE :
                    parentSpan.getParentId();
            exitSpan = new ExitSpan(parentSpanId++, operationName, spanIdGenerator++, currentTracer);
            return push(exitSpan);
        }
        exitSpan.start();
        return exitSpan;
    }

    public void inject(ContextCarrier carrier) {
        carrier.setTraceId(this.tracer.getTraceId());
    }

    public TraceSpan activeSpan() {
        TraceSpan span = peek();
        if (Objects.isNull(span)) {
            throw new IllegalStateException("No active span");
        }
        return span;
    }

    public Boolean stopSpan(TraceSpan span) {
        TraceSpan lastSpan = peek();
        if (lastSpan == span) {
            pop();
        }
        return activeSpans.isEmpty();
    }

    private TraceSpan push(TraceSpan span) {
        activeSpans.add(span);
        return span;
    }

    private TraceSpan pop() {
        return activeSpans.remove(activeSpans.size() - 1);
    }

    private TraceSpan peek() {
        if (activeSpans.isEmpty()) {
            return null;
        }
        return activeSpans.get(activeSpans.size() - 1);
    }
}

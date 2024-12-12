package com.mimu.common.trace;

import com.mimu.common.trace.span.TraceSpan;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Tracer {
    private String traceId;
    private List<TraceSpan> spans;

    public Tracer() {
        this.traceId = GlobalIdGenerator.generate();
    }

    public String getTraceId() {
        return traceId;
    }

    void setTraceId(String id) {
        this.traceId = id;
    }

    public TraceSpan addSpan(TraceSpan span) {
        if (Objects.isNull(spans)) {
            spans = new LinkedList<>();
        } else {
            spans.add(span);
        }
        return span;
    }

}

package com.mimu.common.trace;

import com.mimu.common.trace.id.DistributedId;
import com.mimu.common.trace.span.TraceSpan;

import java.util.LinkedList;
import java.util.List;


public class Tracer {
    private DistributedId traceId;
    private List<TraceSpan> traceSpans = new LinkedList<>();

    public Tracer() {
        this.traceId = new DistributedId();
    }

    public DistributedId getTraceId() {
        return traceId;
    }

    public void setTraceId(DistributedId id) {
        this.traceId = id;
    }

    public void addTraceSpan(TraceSpan traceSpan) {
        this.traceSpans.add(traceSpan);
    }

}

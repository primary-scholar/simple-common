package com.mimu.common.trace;

import com.mimu.common.trace.span.TraceSpan;

import java.util.List;


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

}

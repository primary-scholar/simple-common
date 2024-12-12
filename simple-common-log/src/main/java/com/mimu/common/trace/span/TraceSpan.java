package com.mimu.common.trace.span;


import com.mimu.common.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class TraceSpan implements AbstractSpan {
    private static final Logger IO = LoggerFactory.getLogger("IO");
    protected Tracer tracer;
    protected Integer parentId;
    protected Integer spanId;
    protected String spanName;
    protected List<TraceSpan> refs;
    protected Long startTime;
    protected Long endTime;

    public TraceSpan(Integer parentId, String spanName, Integer spanId, Tracer tracer) {
        this.parentId = parentId;
        this.spanName = spanName;
        this.spanId = spanId;
        this.tracer = tracer;
        this.startTime = System.currentTimeMillis();
    }

    public TraceSpan setSpanName(String spanName) {
        this.spanName = spanName;
        return this;
    }

    public Tracer getTracer() {
        return tracer;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public TraceSpan start() {
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public TraceSpan stop() {
        this.endTime = System.currentTimeMillis();
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}

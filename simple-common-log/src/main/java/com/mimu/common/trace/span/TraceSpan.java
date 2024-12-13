package com.mimu.common.trace.span;


import com.mimu.common.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class TraceSpan implements AbstractSpan {
    private static final Logger IO = LoggerFactory.getLogger("IO");
    protected Tracer tracer;
    protected Integer parentSpanId;
    protected Integer spanId;
    protected String spanName;
    protected Long startTime;
    protected Long endTime;
    protected Map<String, String> tags;

    public TraceSpan(Integer parentSpanId, String spanName, Integer spanId, Tracer tracer) {
        this.parentSpanId = parentSpanId;
        this.spanName = spanName;
        this.spanId = spanId;
        this.tracer = tracer;
        this.startTime = System.currentTimeMillis();
        this.tags = new HashMap<>();
    }

    public TraceSpan setSpanName(String spanName) {
        this.spanName = spanName;
        return this;
    }

    public TraceSpan start() {
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public TraceSpan stop() {
        this.endTime = System.currentTimeMillis();
        return this;
    }

    public void addTag(String key, String value) {
        this.tags.put(key, value);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Tracer getTracer() {
        return tracer;
    }

    public Integer getParentSpanId() {
        return parentSpanId;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}

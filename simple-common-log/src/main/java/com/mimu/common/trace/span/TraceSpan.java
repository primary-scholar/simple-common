package com.mimu.common.trace.span;


import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.Tracer;
import com.mimu.common.trace.id.DistributedId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class TraceSpan implements AbstractSpan {
    private static final Logger IO = LoggerFactory.getLogger("IO");
    protected Tracer tracer;
    protected DistributedId spanId;
    protected Integer parentSpanSequenceId;
    protected Integer spanSequenceId;
    protected String spanName;
    protected Long startTime;
    protected Long endTime;
    protected Map<String, String> tags;

    public TraceSpan(Tracer tracer, Integer parentSpanSequenceId, Integer spanSequenceId, String spanName) {
        this.tracer = tracer;
        this.spanId = new DistributedId();
        this.parentSpanSequenceId = parentSpanSequenceId;
        this.spanSequenceId = spanSequenceId;
        this.spanName = spanName;
        this.startTime = System.currentTimeMillis();
        this.tags = new HashMap<>();
        this.tracer.addTraceSpan(this);
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

    public Integer getParentSpanSequenceId() {
        return parentSpanSequenceId;
    }

    public void setParentSpanSequenceId(Integer parentSpanSequenceId) {
        this.parentSpanSequenceId = parentSpanSequenceId;
        this.tags.put(NounConstant.PARENT_SPAN_SEQUENCE_ID, String.valueOf(parentSpanSequenceId));
    }

    public Integer getSpanSequenceId() {
        return spanSequenceId;
    }

    public void setSpanSequenceId(Integer spanSequenceId) {
        this.spanSequenceId = spanSequenceId;
        this.tags.put(NounConstant.SPAN_SEQUENCE_ID, String.valueOf(spanSequenceId));
    }

    public DistributedId getSpanId() {
        return spanId;
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

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}

package com.mimu.common.trace;

import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;


public class Tracer {
    private String traceId;
    private List<TraceSpan> spans;
    private LinkedList<TraceSpan> activeSpansStack;

    public Tracer() {
    }

    public String getTraceId() {
        return StringUtils.isEmpty(traceId) ? GlobalIdGenerator.generate() : traceId;
    }

    void setTraceId(String id) {
        this.traceId = id;
    }

    protected TraceSpan peek() {
        if (activeSpansStack.isEmpty()) {
            return null;
        }
        return activeSpansStack.getLast();
    }
}

package com.mimu.common.trace.span;


import com.mimu.common.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TraceSpan {
    private static final Logger IO = LoggerFactory.getLogger("IO");
    protected Tracer tracer;
    protected Integer parentId;
    protected Integer spanId;
    protected String spanName;
    protected List<TraceSpan> refs;
    protected Long startTime;
    protected Long endTime;

    public TraceSpan(Integer parentId, String spanName, Integer spanId) {
        this.parentId = parentId;
        this.spanName = spanName;
        this.spanId = spanId;
        this.startTime = System.currentTimeMillis();
    }

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

}

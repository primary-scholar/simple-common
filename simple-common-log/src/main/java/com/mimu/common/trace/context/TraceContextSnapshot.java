package com.mimu.common.trace.context;

import com.mimu.common.trace.id.DistributedId;


public class TraceContextSnapshot {
    private DistributedId traceId;
    private String traceSegmentId;
    private Integer spanId;

    public TraceContextSnapshot(DistributedId traceId, String traceSegmentId, Integer spanId) {
        this.traceId = traceId;
        this.traceSegmentId = traceSegmentId;
        this.spanId = spanId;
    }
}

package com.mimu.common.trace.context;

import com.mimu.common.trace.id.DistributedId;


public class TraceContextSnapshot {
    private DistributedId traceId;
    private DistributedId spanId;
    private Integer spanSequenceId;

    public TraceContextSnapshot(DistributedId traceId, DistributedId spanId, Integer spanSequenceId) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.spanSequenceId = spanSequenceId;
    }
}

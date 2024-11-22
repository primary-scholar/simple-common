package com.mimu.common.constants;

public class TraceContext {

    private TraceSegment segment;

    public TraceContext() {
        this.segment = new TraceSegment();
    }

    public String getTraceId() {
        return segment.getTraceSegmentId();
    }

    public void setTraceId(String requestId) {
        segment.setTraceSegmentId(requestId);
    }
}

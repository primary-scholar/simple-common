package com.mimu.common.constants;

import org.apache.commons.lang3.StringUtils;


public class TraceSegment {
    private String traceSegmentId;

    public TraceSegment() {
    }

    public String getTraceSegmentId() {
        return StringUtils.isEmpty(traceSegmentId) ? GlobalIdGenerator.generate() : traceSegmentId;
    }

    void setTraceSegmentId(String id) {
        this.traceSegmentId = id;
    }
}

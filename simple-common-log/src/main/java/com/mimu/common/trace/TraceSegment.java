package com.mimu.common.trace;

import com.mimu.common.trace.id.DistributedId;
import com.mimu.common.trace.id.GlobalIdGenerator;
import com.mimu.common.trace.span.TraceSpan;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class TraceSegment {
    @Getter
    private DistributedId globalTraceId;
    @Getter
    private final String traceSegmentId;
    private final List<TraceSpan> spanList = new LinkedList<>();

    public TraceSegment() {
        this.traceSegmentId = GlobalIdGenerator.generate();
        this.globalTraceId = new DistributedId();
    }

    public void modifyGlobalTraceId(DistributedId distributedId) {
        if (Objects.isNull(distributedId) || StringUtils.isEmpty(distributedId.getId())) {
            throw new RuntimeException("invalid globalTraceId");

        }
        this.globalTraceId = distributedId;
    }

    public void addTraceSpan(TraceSpan traceSpan) {
        this.spanList.add(traceSpan);
    }

}

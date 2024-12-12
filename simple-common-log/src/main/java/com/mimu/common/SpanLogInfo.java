package com.mimu.common;

import com.mimu.common.trace.span.TraceSpan;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class SpanLogInfo implements Serializable {
    private String traceId;
    private Integer parentId;
    private Integer spanId;
    private Long cid;
    private String remoteInterface;
    private Integer cost;
    private String request;
    private String response;

    public SpanLogInfo(TraceSpan span) {
        if (Objects.isNull(span)) {
            return;
        }
        this.traceId = span.getTracer().getTraceId();
        this.parentId = span.getParentId();
        this.spanId = span.getSpanId();
        this.remoteInterface = null;
        this.cost = Math.toIntExact(span.getEndTime() - span.getStartTime());
    }
}

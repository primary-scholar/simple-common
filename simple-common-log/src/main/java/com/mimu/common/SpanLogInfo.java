package com.mimu.common;

import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.TraceSegment;
import com.mimu.common.trace.span.TraceSpan;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Setter
@Getter
public class SpanLogInfo implements Serializable {
    private String traceId;
    private Integer parentSpanId;
    private Integer spanId;
    private Long cid;
    private String remoteInterface;
    private Long starTime;
    private Integer cost;
    private String request;
    private String response;

    public SpanLogInfo(TraceSpan span, TraceSegment traceSegment) {
        if (Objects.isNull(span)) {
            return;
        }
        Map<String, String> tags = span.getTags();
        this.traceId = traceSegment.getGlobalTraceId().getId();
        this.parentSpanId = span.getParentSpanId();
        this.spanId = span.getSpanId();
        this.cid = NumberUtils.toLong(tags.get(NounConstant.CID));
        this.remoteInterface = tags.get(NounConstant.URI);
        this.starTime = span.getStartTime();
        this.cost = Math.toIntExact(span.getEndTime() - span.getStartTime());
        this.request = tags.get(NounConstant.REQUEST);
        this.response = tags.get(NounConstant.RESPONSE);
    }
}

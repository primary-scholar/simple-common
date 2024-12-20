package com.mimu.common;

import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.Tracer;
import com.mimu.common.trace.span.TraceSpan;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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

    public SpanLogInfo(TraceSpan span) {
        if (Objects.isNull(span)) {
            return;
        }
        Map<String, String> tags = span.getTags();
        Tracer tracer = span.getTracer();
        this.traceId = Objects.isNull(tracer) ? StringUtils.EMPTY : tracer.getTraceId().getId();
        this.parentSpanId = span.getParentSpanSequenceId();
        this.spanId = span.getSpanSequenceId();
        this.cid = NumberUtils.toLong(tags.get(NounConstant.CID));
        this.remoteInterface = tags.get(NounConstant.URI);
        this.starTime = span.getStartTime();
        this.cost = Math.toIntExact(span.getEndTime() - span.getStartTime());
        this.request = tags.get(NounConstant.REQUEST);
        this.response = tags.get(NounConstant.RESPONSE);
    }
}

package com.mimu.common.trace.context;

import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.id.DistributedId;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

public class TraceContextCarrier implements Serializable {
    @Getter
    private DistributedId traceId;
    @Getter
    private Integer parentSpanId;
    @Getter
    private Integer spanId;
    private final Map<String, String> tagValueMap = new HashMap<>();

    public Map<String, String> emptyTags() {
        tagValueMap.put(NounConstant.TRACE_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.PARENT_SPAN_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.SPAN_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.URI, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.QUERY, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.REQUEST, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.RESPONSE, StringUtils.EMPTY);
        return tagValueMap;
    }

    public Map<String, String> tags() {
        return tagValueMap;
    }


    public void setTraceId(DistributedId traceId) {
        this.traceId = traceId;
        this.tagValueMap.put(NounConstant.TRACE_ID, Objects.isNull(traceId) ? StringUtils.EMPTY : traceId.getId());
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
        this.tagValueMap.put(NounConstant.SPAN_ID, String.valueOf(spanId));
    }

    public void setParentSpanId(Integer parentSpanId) {
        this.parentSpanId = parentSpanId;
        this.tagValueMap.put(NounConstant.PARENT_SPAN_ID, String.valueOf(parentSpanId));
    }

    public Boolean isValid() {
        return Objects.nonNull(traceId) && Objects.nonNull(parentSpanId) && Objects.nonNull(spanId);
    }
}

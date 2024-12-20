package com.mimu.common.trace.context;

import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.id.DistributedId;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

public class ContextCarrier implements Serializable {
    private DistributedId traceId;
    private Integer parentSpanSequenceId;
    private Integer spanSequenceId;
    private final Map<String, String> tagValueMap = new HashMap<>();

    public Map<String, String> emptyTags() {
        tagValueMap.put(NounConstant.TRACE_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.PARENT_SPAN_SEQUENCE_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.SPAN_SEQUENCE_ID, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.URI, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.REQUEST, StringUtils.EMPTY);
        tagValueMap.put(NounConstant.RESPONSE, StringUtils.EMPTY);
        return tagValueMap;
    }

    public Map<String, String> tags() {
        return tagValueMap;
    }


    public DistributedId getTraceId() {
        return traceId;
    }

    public void setTraceId(DistributedId traceId) {
        this.traceId = traceId;
        this.tagValueMap.put(NounConstant.TRACE_ID, Objects.isNull(traceId) ? StringUtils.EMPTY : traceId.getId());
    }

    public Integer getSpanSequenceId() {
        return spanSequenceId;
    }

    public void setSpanSequenceId(Integer spanSequenceId) {
        this.spanSequenceId = spanSequenceId;
        this.tagValueMap.put(NounConstant.SPAN_SEQUENCE_ID, String.valueOf(spanSequenceId));
    }

    public Integer getParentSpanSequenceId() {
        return parentSpanSequenceId;
    }

    public void setParentSpanSequenceId(Integer parentSpanSequenceId) {
        this.parentSpanSequenceId = parentSpanSequenceId;
        this.tagValueMap.put(NounConstant.PARENT_SPAN_SEQUENCE_ID, String.valueOf(parentSpanSequenceId));
    }

    public Boolean isValid() {
        return Objects.nonNull(traceId) && Objects.nonNull(parentSpanSequenceId) && Objects.nonNull(spanSequenceId);
    }
}

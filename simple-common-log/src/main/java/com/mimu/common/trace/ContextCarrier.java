package com.mimu.common.trace;

import com.mimu.common.constants.NounConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.*;

public class ContextCarrier implements Serializable {
    public static final Integer DEFAULT_SPAN_ID = NumberUtils.INTEGER_ZERO;
    private String traceId;
    private Integer spanId;
    private Map<String, String> tagValueMap = new HashMap<>();

    public Map<String, String> tags() {
        tagValueMap.put(NounConstant.TRACE_ID, StringUtils.EMPTY);
        return tagValueMap;
    }


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
        this.tagValueMap.put(NounConstant.TRACE_ID, traceId);
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
        this.tagValueMap.put(NounConstant.SPAN_ID, String.valueOf(spanId));
    }

    public Boolean isValid() {
        return StringUtils.isNotBlank(traceId) && Objects.nonNull(spanId);
    }
}

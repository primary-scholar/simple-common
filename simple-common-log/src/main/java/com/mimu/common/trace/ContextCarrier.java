package com.mimu.common.trace;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

public class ContextCarrier implements Serializable {
    private String traceId;
    private Integer spanId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
    }

    public Boolean isValid() {
        return StringUtils.isNotBlank(traceId) && spanId > NumberUtils.INTEGER_MINUS_ONE;
    }
}

package com.mimu.common.trace.context;

import com.mimu.common.trace.id.DistributedId;
import lombok.Getter;

import java.util.Objects;


@Getter
public class TraceContextSnapshot {
    private DistributedId traceId;
    private DistributedId spanId;
    private Integer parentSpanSequenceId;
    private Integer spanSequenceId;

    public TraceContextSnapshot(DistributedId traceId, DistributedId spanId, Integer parentSpanSequenceId, Integer spanSequenceId) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanSequenceId = parentSpanSequenceId;
        this.spanSequenceId = spanSequenceId;
    }

    public Boolean isValid() {
        return Objects.nonNull(traceId) && Objects.nonNull(spanId) && Objects.nonNull(spanSequenceId);
    }

    public Boolean isFromCurrent() {
        return Objects.nonNull(spanId) && spanId.equals(ContextManager.capture().getSpanId());
    }

}

package com.mimu.common.trace.context;

import com.mimu.common.SpanLogInfo;
import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.Tracer;
import com.mimu.common.trace.span.EntrySpan;
import com.mimu.common.trace.span.ExitSpan;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TraceContext {
    private static final Logger IO = LoggerFactory.getLogger("IO");
    private Tracer tracer;
    private Integer spanIdGenerator;
    private List<TraceSpan> activeSpans = new LinkedList<>();

    public TraceContext() {
        this.tracer = new Tracer();
        this.spanIdGenerator = 0;
    }

    public String getTraceId() {
        return tracer.getTraceId();
    }

    public void setTraceId(String requestId) {
        tracer.setTraceId(requestId);
    }

    public TraceSpan createEntrySpan(String operationName) {
        TraceSpan entrySpan;
        TraceSpan parentSpan = peek();
        Tracer currentTracer = this.tracer;
        if (Objects.nonNull(parentSpan) && parentSpan.isEntry()) {
            parentSpan.setSpanName(operationName);
            entrySpan = parentSpan;
            entrySpan.start();
        } else {
            Integer parentSpanId = Objects.isNull(parentSpan) ? NumberUtils.INTEGER_MINUS_ONE :
                    parentSpan.getParentSpanId();
            entrySpan = new EntrySpan(parentSpanId++, operationName, spanIdGenerator++, currentTracer);
            entrySpan.start();
            push(entrySpan);
        }
        fillLogMdcInfo();
        return entrySpan;
    }

    public void extract(ContextCarrier carrier, TraceSpan span) {
        this.tracer.setTraceId(carrier.getTraceId());
        span.setParentSpanId(carrier.getParentSpanId());
        span.setSpanId(carrier.getSpanId());
        fillLogMdcInfo();
    }

    public TraceSpan createExitSpan(String operationName, String peer) {
        TraceSpan exitSpan;
        TraceSpan parentSpan = peek();
        Tracer currentTracer = this.tracer;
        if (Objects.nonNull(parentSpan) && parentSpan.isExit()) {
            exitSpan = parentSpan;
        } else {
            Integer parentSpanId = Objects.isNull(parentSpan) ? NumberUtils.INTEGER_MINUS_ONE :
                    parentSpan.getParentSpanId();
            exitSpan = new ExitSpan(parentSpanId++, operationName, spanIdGenerator++, currentTracer);
            push(exitSpan);
        }
        exitSpan.start();
        fillLogMdcInfo();
        return exitSpan;
    }

    public void inject(ContextCarrier carrier, TraceSpan span) {
        carrier.setTraceId(this.tracer.getTraceId());
        carrier.setParentSpanId(span.getParentSpanId());
        carrier.setSpanId(span.getSpanId());
        fillLogMdcInfo();
    }

    public TraceSpan activeSpan() {
        TraceSpan span = peek();
        if (Objects.isNull(span)) {
            throw new IllegalStateException("No active span");
        }
        return span;
    }

    public Boolean stopSpan(TraceSpan span) {
        TraceSpan lastSpan = peek();
        if (lastSpan == span) {
            pop();
        }
        finish(lastSpan);
        return activeSpans.isEmpty();
    }

    private TraceSpan push(TraceSpan span) {
        activeSpans.add(span);
        return span;
    }

    private TraceSpan pop() {
        return activeSpans.remove(activeSpans.size() - 1);
    }

    private TraceSpan peek() {
        if (activeSpans.isEmpty()) {
            return null;
        }
        return activeSpans.get(activeSpans.size() - 1);
    }

    private void finish(TraceSpan span) {
        if (Objects.isNull(span)) {
            return;
        }
        span.stop();
        SpanLogInfo spanLogInfo = new SpanLogInfo(span);
        MDC.put(NounConstant.TRACE_ID, spanLogInfo.getTraceId());
        MDC.put(NounConstant.CID, String.valueOf(spanLogInfo.getCid()));
        MDC.put(NounConstant.PARENT_SPAN_ID, String.valueOf(spanLogInfo.getParentSpanId()));
        MDC.put(NounConstant.SPAN_ID, String.valueOf(spanLogInfo.getSpanId()));
        MDC.put(NounConstant.URI, spanLogInfo.getRemoteInterface());
        MDC.put(NounConstant.START_TIME, String.valueOf(spanLogInfo.getStarTime()));
        MDC.put(NounConstant.COST, String.valueOf(spanLogInfo.getCost()));
        MDC.put(NounConstant.REQUEST, spanLogInfo.getRequest());
        MDC.put(NounConstant.RESPONSE, spanLogInfo.getResponse());
        IO.info("");
        MDC.remove(NounConstant.TRACE_ID);
        MDC.remove(NounConstant.CID);
        MDC.remove(NounConstant.PARENT_SPAN_ID);
        MDC.remove(NounConstant.SPAN_ID);
        MDC.remove(NounConstant.URI);
        MDC.remove(NounConstant.START_TIME);
        MDC.remove(NounConstant.COST);
        MDC.remove(NounConstant.REQUEST);
        MDC.remove(NounConstant.RESPONSE);
    }

    private void fillLogMdcInfo() {
        MDC.put(NounConstant.TRACE_ID, this.tracer.getTraceId());
    }

}

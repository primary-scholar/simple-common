package com.mimu.common.log.dubbo;

import com.alibaba.fastjson.JSONObject;
import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.context.TraceContextCarrier;
import com.mimu.common.trace.context.TraceContextManager;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;

import java.util.Map;
import java.util.Objects;

public class AbstractTraceFilter {

    public void extractCarrier(Invocation invocation, TraceContextCarrier carrier) {
        Map<String, String> attachments = invocation.getAttachments();
        Map<String, String> tags = carrier.tags();
        attachments.putAll(tags);
    }

    public void fillCarrier(Invocation invocation, TraceContextCarrier carrier) {
        Map<String, String> attachments = invocation.getAttachments();
        Map<String, String> tags = carrier.emptyTags();
        tags.replaceAll((s, s2) -> attachments.getOrDefault(s, s2));
        if (Objects.isNull(carrier.getTraceId())) {
            String traceId = tags.get(NounConstant.TRACE_ID);
            if (StringUtils.isEmpty(traceId)) {
                traceId = "";
            }
            carrier.setTraceId(TraceContextManager.createDistributedId(traceId));
        }
        if (Objects.isNull(carrier.getParentSpanId())) {
            String parentSpanId = tags.get(NounConstant.PARENT_SPAN_ID);
            if (StringUtils.isEmpty(parentSpanId)) {
                parentSpanId = attachments.get(NounConstant.PARENT_SPAN_ID);
                if (StringUtils.isEmpty(parentSpanId)) {
                    parentSpanId = String.valueOf(NounConstant.DEFAULT_PARENT_SPAN_ID);
                }
            }
            carrier.setParentSpanId(NumberUtils.toInt(parentSpanId, NounConstant.DEFAULT_PARENT_SPAN_ID));
        }
        if (Objects.isNull(carrier.getSpanId())) {
            String spanId = tags.get(NounConstant.SPAN_ID);
            if (StringUtils.isEmpty(spanId)) {
                spanId = attachments.get(NounConstant.SPAN_ID);
                if (StringUtils.isEmpty(spanId)) {
                    spanId = String.valueOf(NounConstant.DEFAULT_SPAN_ID);
                }
            }
            carrier.setSpanId(NumberUtils.toInt(spanId, NounConstant.DEFAULT_SPAN_ID));
        }
    }

    public void fillSpanTag(Invocation invocation, TraceSpan span) {
        Map<String, String> tags = span.getTags();
        Class<?> anInterface = invocation.getInvoker().getInterface();
        Object[] arguments = invocation.getArguments();
        if (StringUtils.isEmpty(tags.get(NounConstant.URI))) {
            span.addTag(NounConstant.URI, anInterface.getCanonicalName());
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.QUERY))) {
            span.addTag(NounConstant.QUERY, anInterface.getCanonicalName());
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.REQUEST))) {
            span.addTag(NounConstant.REQUEST, "");
        }
        String tagCid = tags.get(NounConstant.CID);
        if (StringUtils.isEmpty(tagCid) || NumberUtils.toLong(tagCid) <= NumberUtils.LONG_ZERO) {
            span.addTag(NounConstant.CID, "");
        }
    }

    public void fillSpanTag(Invocation invocation, TraceSpan span, Result result) {
        String returnValue = StringUtils.EMPTY;
        if (Objects.nonNull(result)) {
            returnValue = JSONObject.toJSONString(result.getValue());
        }
        fillSpanTag(invocation, span);
        Map<String, String> tags = span.getTags();
        if (StringUtils.isEmpty(tags.get(NounConstant.RESPONSE))) {
            span.addTag(NounConstant.RESPONSE, returnValue);
        }
    }


}
